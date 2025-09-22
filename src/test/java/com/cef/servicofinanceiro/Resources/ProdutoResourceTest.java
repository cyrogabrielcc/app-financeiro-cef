package com.cef.servicofinanceiro.Resources;

import com.cef.servicofinanceiro.resources.ProdutoResource;
import com.cef.servicofinanceiro.dto.ProdutoRequestDTO;
import com.cef.servicofinanceiro.dto.ProdutoResponseDTO;
import com.cef.servicofinanceiro.service.ProdutoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoResourceTest {

    @Test
    @DisplayName("Deve criar produto e retornar status 201 com localização")
    void deveCriarProdutoComSucesso() {
        ProdutoService service = mock(ProdutoService.class);
        ProdutoResource resource = new ProdutoResource(service);

        ProdutoRequestDTO request = new ProdutoRequestDTO();
        request.setNome("Crédito Consignado");
        request.setTaxaJurosAnual(new BigDecimal("12.5"));
        request.setPrazoMaximoMeses(48);

        ProdutoResponseDTO responseDTO = ProdutoResponseDTO.builder()
                .id(10L)
                .nome("Crédito Consignado")
                .taxaJurosAnual(new BigDecimal("12.5"))
                .prazoMaximoMeses(48)
                .build();

        when(service.criar(any())).thenReturn(responseDTO);

        Response response = resource.criar(request);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        assertTrue(response.getLocation().toString().endsWith("/10"));
    }

    @Test
    @DisplayName("Deve listar todos os produtos e retornar status 200")
    void deveListarTodosProdutosComSucesso() {
        ProdutoService service = mock(ProdutoService.class);
        ProdutoResource resource = new ProdutoResource(service);

        ProdutoResponseDTO produto1 = ProdutoResponseDTO.builder()
                .id(1L)
                .nome("Produto 1")
                .taxaJurosAnual(new BigDecimal("10.0"))
                .prazoMaximoMeses(12)
                .build();

        ProdutoResponseDTO produto2 = ProdutoResponseDTO.builder()
                .id(2L)
                .nome("Produto 2")
                .taxaJurosAnual(new BigDecimal("15.0"))
                .prazoMaximoMeses(24)
                .build();

        when(service.listarTodos()).thenReturn(Arrays.asList(produto1, produto2));

        Response response = resource.listarTodos();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<ProdutoResponseDTO> produtos = (List<ProdutoResponseDTO>) response.getEntity();
        assertEquals(2, produtos.size());
        assertEquals("Produto 1", produtos.get(0).getNome());
        assertEquals("Produto 2", produtos.get(1).getNome());
    }

    @Test
    @DisplayName("Deve buscar produto por ID e retornar status 200")
    void deveBuscarProdutoPorIdComSucesso() {
        ProdutoService service = mock(ProdutoService.class);
        ProdutoResource resource = new ProdutoResource(service);

        ProdutoResponseDTO produto = ProdutoResponseDTO.builder()
                .id(5L)
                .nome("Produto Teste")
                .taxaJurosAnual(new BigDecimal("20.0"))
                .prazoMaximoMeses(36)
                .build();

        when(service.buscarPorId(5L)).thenReturn(produto);

        Response response = resource.buscarPorId(5L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ProdutoResponseDTO entity = (ProdutoResponseDTO) response.getEntity();
        assertEquals(5L, entity.getId());
        assertEquals("Produto Teste", entity.getNome());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar produto inexistente")
    void deveRetornar404ProdutoNaoEncontrado() {
        ProdutoService service = mock(ProdutoService.class);
        ProdutoResource resource = new ProdutoResource(service);

        when(service.buscarPorId(99L)).thenThrow(new jakarta.persistence.EntityNotFoundException("Produto não encontrado"));

        try {
            resource.buscarPorId(99L);
            fail("Deveria lançar EntityNotFoundException");
        } catch (jakarta.persistence.EntityNotFoundException e) {
            assertTrue(e.getMessage().contains("Produto não encontrado"));
        }
    }
}