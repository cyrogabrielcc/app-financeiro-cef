package com.cef.servicofinanceiro.dto;


import java.math.BigDecimal;
import java.util.List;

public class EmprestimoResponseDTO {
    private ProdutoResponseDTO produto;
    private BigDecimal valorSolicitado;
    private int prazoMeses;
    private BigDecimal taxaJurosEfetivaMensal;
    private BigDecimal valorTotalComJuros;
    private BigDecimal parcelaMensal; // RENOMEADO (era valorDaParcela)
    private List<ParcelaCalculoDTO> memoriaCalculo; // RENOMEADO (era memoriaDeCalculo)

    // O campo taxaJurosAnual foi removido para corresponder exatamente ao seu exemplo

    private EmprestimoResponseDTO(ProdutoResponseDTO produto, BigDecimal valorSolicitado, int prazoMeses, BigDecimal taxaJurosEfetivaMensal, BigDecimal valorTotalComJuros, BigDecimal parcelaMensal, List<ParcelaCalculoDTO> memoriaCalculo) {
        this.produto = produto;
        this.valorSolicitado = valorSolicitado;
        this.prazoMeses = prazoMeses;
        this.taxaJurosEfetivaMensal = taxaJurosEfetivaMensal;
        this.valorTotalComJuros = valorTotalComJuros;
        this.parcelaMensal = parcelaMensal;
        this.memoriaCalculo = memoriaCalculo;
    }

    // Getters
    public ProdutoResponseDTO getProduto() { return produto; }
    public BigDecimal getValorSolicitado() { return valorSolicitado; }
    public int getPrazoMeses() { return prazoMeses; }
    public BigDecimal getTaxaJurosEfetivaMensal() { return taxaJurosEfetivaMensal; }
    public BigDecimal getValorTotalComJuros() { return valorTotalComJuros; }
    public BigDecimal getParcelaMensal() { return parcelaMensal; }
    public List<ParcelaCalculoDTO> getMemoriaCalculo() { return memoriaCalculo; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ProdutoResponseDTO produto;
        private BigDecimal valorSolicitado;
        private int prazoMeses;
        private BigDecimal taxaJurosEfetivaMensal;
        private BigDecimal valorTotalComJuros;
        private BigDecimal parcelaMensal;
        private List<ParcelaCalculoDTO> memoriaCalculo;

        public Builder produto(ProdutoResponseDTO produto) { this.produto = produto; return this; }
        public Builder valorSolicitado(BigDecimal valorSolicitado) { this.valorSolicitado = valorSolicitado; return this; }
        public Builder prazoMeses(int prazoMeses) { this.prazoMeses = prazoMeses; return this; }
        public Builder taxaJurosEfetivaMensal(BigDecimal taxaJurosEfetivaMensal) { this.taxaJurosEfetivaMensal = taxaJurosEfetivaMensal; return this; }
        public Builder valorTotalComJuros(BigDecimal valorTotalComJuros) { this.valorTotalComJuros = valorTotalComJuros; return this; }
        public Builder parcelaMensal(BigDecimal parcelaMensal) { this.parcelaMensal = parcelaMensal; return this; }
        public Builder memoriaCalculo(List<ParcelaCalculoDTO> memoriaCalculo) { this.memoriaCalculo = memoriaCalculo; return this; }

        public EmprestimoResponseDTO build() {
            return new EmprestimoResponseDTO(produto, valorSolicitado, prazoMeses, taxaJurosEfetivaMensal, valorTotalComJuros, parcelaMensal, memoriaCalculo);
        }
    }
}