package com.cef.servicofinanceiro.service;



import com.cef.servicofinanceiro.dto.EmprestimoRequestDTO;
import com.cef.servicofinanceiro.dto.EmprestimoResponseDTO;
import com.cef.servicofinanceiro.dto.ParcelaCalculoDTO;
import com.cef.servicofinanceiro.model.Produto;
import com.cef.servicofinanceiro.exception.RegraDeNegocioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmprestimoService {

    private final ProdutoService produtoService;
    private static final MathContext MC = new MathContext(12, RoundingMode.HALF_UP);
    private static final int SCALE_MONETARIO = 2;
    private static final int SCALE_TAXA = 6;

    @Inject
    public EmprestimoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public EmprestimoResponseDTO calcular(EmprestimoRequestDTO request) {
        Produto produto = produtoService.findProdutoById(request.getIdProduto());

        if (request.getPrazoMeses() > produto.getPrazoMaximoMeses()) {
            throw new RegraDeNegocioException("O prazo solicitado (" + request.getPrazoMeses() +
                    " meses) excede o máximo de " + produto.getPrazoMaximoMeses() + " meses para este produto.");
        }

        BigDecimal taxaMensal = calcularTaxaMensal(produto.getTaxaJurosAnual());
        BigDecimal pmt = calcularParcelaPrice(request.getValorSolicitado(), taxaMensal, request.getPrazoMeses());
        List<ParcelaCalculoDTO> memoriaCalculo = gerarMemoriaDeCalculo(request, pmt, taxaMensal);

        BigDecimal valorTotalComJuros = pmt.multiply(BigDecimal.valueOf(request.getPrazoMeses())).setScale(SCALE_MONETARIO, RoundingMode.HALF_UP);

        return EmprestimoResponseDTO.builder()
                .produto(produtoService.buscarPorId(produto.getId()))
                .valorSolicitado(request.getValorSolicitado())
                .prazoMeses(request.getPrazoMeses())
                .taxaJurosEfetivaMensal(taxaMensal.setScale(SCALE_TAXA, RoundingMode.HALF_UP))
                .valorTotalComJuros(valorTotalComJuros)
                .parcelaMensal(pmt)
                .memoriaCalculo(memoriaCalculo)
                .build();
    }

    private BigDecimal calcularTaxaMensal(BigDecimal taxaAnual) {
        BigDecimal taxaAnualDecimal = taxaAnual.divide(BigDecimal.valueOf(100), MC);
        double base = BigDecimal.ONE.add(taxaAnualDecimal).doubleValue();
        double expoente = 1.0 / 12.0;
        BigDecimal fatorMensal = BigDecimal.valueOf(Math.pow(base, expoente));
        return fatorMensal.subtract(BigDecimal.ONE);
    }

    private BigDecimal calcularParcelaPrice(BigDecimal pv, BigDecimal i, int n) {
        BigDecimal umMaisI = BigDecimal.ONE.add(i);
        BigDecimal umMaisIelevadoN = umMaisI.pow(n, MC);
        BigDecimal pmt = pv.multiply(i.multiply(umMaisIelevadoN)).divide(umMaisIelevadoN.subtract(BigDecimal.ONE), MC);
        return pmt.setScale(SCALE_MONETARIO, RoundingMode.HALF_UP);
    }

    // --- LÓGICA DE CÁLCULO ATUALIZADA E CORRIGIDA ---
    private List<ParcelaCalculoDTO> gerarMemoriaDeCalculo(EmprestimoRequestDTO request, BigDecimal pmt, BigDecimal taxaMensal) {
        List<ParcelaCalculoDTO> memoria = new ArrayList<>();
        BigDecimal saldoDevedorAtual = request.getValorSolicitado();

        for (int mes = 1; mes <= request.getPrazoMeses(); mes++) {
            BigDecimal saldoDevedorInicialDoMes = saldoDevedorAtual;

            BigDecimal jurosMes = saldoDevedorInicialDoMes.multiply(taxaMensal, MC).setScale(SCALE_MONETARIO, RoundingMode.HALF_UP);
            BigDecimal amortizacao;

            // --- AQUI ESTÁ A CORREÇÃO PRINCIPAL ---
            if (mes == request.getPrazoMeses()) {
                // Na última parcela, a amortização é TUDO o que resta do saldo devedor para garantir que zere.
                amortizacao = saldoDevedorInicialDoMes;
            } else {
                // Nas parcelas normais, a amortização é o valor da parcela menos os juros.
                amortizacao = pmt.subtract(jurosMes);
            }
            // --- FIM DA CORREÇÃO PRINCIPAL ---

            BigDecimal saldoDevedorFinalDoMes = saldoDevedorInicialDoMes.subtract(amortizacao);

            memoria.add(ParcelaCalculoDTO.builder()
                    .mes(mes)
                    .saldoDevedorInicial(saldoDevedorInicialDoMes)
                    .juros(jurosMes)
                    .amortizacao(amortizacao)
                    .saldoDevedorFinal(saldoDevedorFinalDoMes.setScale(SCALE_MONETARIO, RoundingMode.HALF_UP))
                    .build());

            // Prepara o saldo devedor para a próxima iteração
            saldoDevedorAtual = saldoDevedorFinalDoMes;
        }
        return memoria;
    }
}