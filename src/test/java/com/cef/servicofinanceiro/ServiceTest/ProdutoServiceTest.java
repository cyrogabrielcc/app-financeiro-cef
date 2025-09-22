package com.cef.servicofinanceiro.ServiceTest;

import com.cef.servicofinanceiro.dto.ProdutoRequestDTO;
import com.cef.servicofinanceiro.dto.ProdutoResponseDTO;
import com.cef.servicofinanceiro.model.Produto;
import com.cef.servicofinanceiro.repository.ProdutoRepository;
import com.cef.servicofinanceiro.service.ProdutoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("Deve criar um produto e retornar o DTO")
    void deveCriarProduto() {
        ProdutoRequestDTO request = new ProdutoRequestDTO();
        request.setNome("Crédito Pessoal");
        request.setTaxaJurosAnual(new BigDecimal("18.00"));
        request.setPrazoMaximoMeses(36);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome(request.getNome());
        produto.setTaxaJurosAnual(request.getTaxaJurosAnual());
        produto.setPrazoMaximoMeses(request.getPrazoMaximoMeses());

        doAnswer(invocation -> {
            Produto p = invocation.getArgument(0);
            p.setId(1L);
            return null;
        }).when(produtoRepository).persist(any(Produto.class));

        ProdutoResponseDTO response = produtoService.criar(request);

        assertNotNull(response);
        assertEquals("Crédito Pessoal", response.getNome());
        assertEquals(new BigDecimal("18.00"), response.getTaxaJurosAnual());
        assertEquals(36, response.getPrazoMaximoMeses());
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodosProdutos() {
        Produto produto1 = new Produto(1L, "Produto 1", new BigDecimal("10.00"), 12);
        Produto produto2 = new Produto(2L, "Produto 2", new BigDecimal("15.00"), 24);

        when(produtoRepository.listAll()).thenReturn(Arrays.asList(produto1, produto2));

        List<ProdutoResponseDTO> produtos = produtoService.listarTodos();

        assertEquals(2, produtos.size());
        assertEquals("Produto 1", produtos.get(0).getNome());
        assertEquals("Produto 2", produtos.get(1).getNome());
    }

    @Test
    @DisplayName("Deve buscar produto por ID")
    void deveBuscarProdutoPorId() {
        Produto produto = new Produto(1L, "Produto Teste", new BigDecimal("20.00"), 36);
        when(produtoRepository.findByIdOptional(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO response = produtoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Produto Teste", response.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto inexistente")
    void deveLancarExcecaoProdutoNaoEncontrado() {
        when(produtoRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            produtoService.buscarPorId(99L);
        });

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }
}