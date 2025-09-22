package com.cef.servicofinanceiro.ServiceTest; // Ajuste para o seu pacote

import com.cef.servicofinanceiro.dto.EmprestimoRequestDTO;
import com.cef.servicofinanceiro.dto.EmprestimoResponseDTO;
import com.cef.servicofinanceiro.dto.ProdutoResponseDTO;
import com.cef.servicofinanceiro.model.Produto;
import com.cef.servicofinanceiro.exception.RegraDeNegocioException;
import com.cef.servicofinanceiro.service.EmprestimoService;
import com.cef.servicofinanceiro.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private EmprestimoService emprestimoService;

    @Test
    @DisplayName("Deve calcular um empréstimo com sucesso e retornar os valores corretos")
    void deveCalcularEmprestimoComSucesso() {
        // Arrange (Preparação)
        Produto mockProduto = new Produto(1L, "Crédito Pessoal", new BigDecimal("18.00"), 36);
        ProdutoResponseDTO mockProdutoDTO = ProdutoResponseDTO.builder().id(1L).build();
        when(produtoService.findProdutoById(1L)).thenReturn(mockProduto);
        when(produtoService.buscarPorId(1L)).thenReturn(mockProdutoDTO);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(1L);
        request.setValorSolicitado(new BigDecimal("10000.00"));
        request.setPrazoMeses(12);

        // Act (Ação)
        EmprestimoResponseDTO response = emprestimoService.calcular(request);

        // Assert (Verificação)
        assertNotNull(response);

        // --- VALORES DE ASSERÇÃO CORRIGIDOS ---
        // Estes são os valores corretos para uma taxa de 18% a.a.
        BigDecimal parcelaEsperada = new BigDecimal("910.46");
        BigDecimal totalEsperado = new BigDecimal("10925.52"); // 910.46 * 12
        BigDecimal taxaMensalEsperada = new BigDecimal("0.013888");

        // Verificamos se a diferença é menor que 1 centavo, o que é mais robusto
        assertTrue(
                parcelaEsperada.subtract(response.getParcelaMensal()).abs().compareTo(new BigDecimal("0.01")) < 0,
                "Valor da parcela difere do esperado. Esperado: " + parcelaEsperada + ", Atual: " + response.getParcelaMensal()
        );

        assertTrue(
                totalEsperado.subtract(response.getValorTotalComJuros()).abs().compareTo(new BigDecimal("0.01")) < 0,
                "Valor total difere do esperado. Esperado: " + totalEsperado + ", Atual: " + response.getValorTotalComJuros()
        );

        assertTrue(
                taxaMensalEsperada.subtract(response.getTaxaJurosEfetivaMensal()).abs().compareTo(new BigDecimal("0.000001")) < 0,
                "Taxa mensal difere do esperado. Esperada: " + taxaMensalEsperada + ", Atual: " + response.getTaxaJurosEfetivaMensal()
        );

        BigDecimal saldoFinal = response.getMemoriaCalculo().get(11).getSaldoDevedorFinal();
        assertTrue(
                saldoFinal.abs().compareTo(new BigDecimal("0.01")) < 0,
                "O saldo devedor final deveria ser próximo de zero, mas foi: " + saldoFinal
        );
    }

    @Test
    @DisplayName("Deve lançar RegraDeNegocioException quando o prazo solicitado for maior que o permitido")
    void deveLancarExcecaoParaPrazoExcedido() {
        // Arrange
        Produto mockProduto = new Produto(1L, "Crédito Pessoal", new BigDecimal("18.00"), 36);
        when(produtoService.findProdutoById(1L)).thenReturn(mockProduto);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(1L);
        request.setValorSolicitado(new BigDecimal("10000.00"));
        request.setPrazoMeses(40);

        // Act & Assert
        RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class, () -> {
            emprestimoService.calcular(request);
        });

        assertTrue(exception.getMessage().contains("excede o máximo de 36 meses"));
    }

    @Test
    @DisplayName("Deve calcular empréstimo com taxa zero")
    void deveCalcularEmprestimoComTaxaZero() {
        Produto mockProduto = new Produto(2L, "Crédito Zero", BigDecimal.ZERO, 24);
        when(produtoService.findProdutoById(2L)).thenReturn(mockProduto);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(2L);
        request.setValorSolicitado(new BigDecimal("12000.00"));
        request.setPrazoMeses(12);

        EmprestimoResponseDTO response = emprestimoService.calcular(request);

        assertEquals(new BigDecimal("1000.00"), response.getParcelaMensal());
        assertEquals(new BigDecimal("12000.00"), response.getValorTotalComJuros());
        assertEquals(BigDecimal.ZERO.setScale(6), response.getTaxaJurosEfetivaMensal());
    }

    @Test
    @DisplayName("Deve lançar exceção para valor solicitado negativo")
    void deveLancarExcecaoParaValorNegativo() {
        Produto mockProduto = new Produto(3L, "Crédito Negativo", new BigDecimal("10.00"), 12);
        when(produtoService.findProdutoById(3L)).thenReturn(mockProduto);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(3L);
        request.setValorSolicitado(new BigDecimal("-5000.00"));
        request.setPrazoMeses(6);

        assertThrows(RegraDeNegocioException.class, () -> emprestimoService.calcular(request));
    }

    @Test
    @DisplayName("Deve lançar exceção para prazo zero")
    void deveLancarExcecaoParaPrazoZero() {
        Produto mockProduto = new Produto(4L, "Crédito Prazo Zero", new BigDecimal("10.00"), 12);
        when(produtoService.findProdutoById(4L)).thenReturn(mockProduto);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(4L);
        request.setValorSolicitado(new BigDecimal("5000.00"));
        request.setPrazoMeses(0);

        assertThrows(RegraDeNegocioException.class, () -> emprestimoService.calcular(request));
    }

    @Test
    @DisplayName("Deve calcular empréstimo para prazo mínimo (1 mês)")
    void deveCalcularEmprestimoPrazoMinimo() {
        Produto mockProduto = new Produto(5L, "Crédito Rápido", new BigDecimal("12.00"), 12);
        when(produtoService.findProdutoById(5L)).thenReturn(mockProduto);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(5L);
        request.setValorSolicitado(new BigDecimal("1000.00"));
        request.setPrazoMeses(1);

        EmprestimoResponseDTO response = emprestimoService.calcular(request);

        assertNotNull(response);
        assertTrue(response.getParcelaMensal().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(1, response.getMemoriaCalculo().size());
    }
}