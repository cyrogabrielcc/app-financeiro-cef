package com.cef.servicofinanceiro.Resources;

import com.cef.servicofinanceiro.resources.EmprestimoResource;
import com.cef.servicofinanceiro.dto.EmprestimoRequestDTO;
import com.cef.servicofinanceiro.dto.EmprestimoResponseDTO;
import com.cef.servicofinanceiro.service.EmprestimoService;
import com.cef.servicofinanceiro.exception.RegraDeNegocioException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmprestimoResourceTest {

    @Test
    @DisplayName("Deve retornar 200 na simulação de empréstimo")
    void deveSimularEmprestimoComSucesso() {
        EmprestimoService service = mock(EmprestimoService.class);
        EmprestimoResource resource = new EmprestimoResource(service);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(1L);
        request.setValorSolicitado(new BigDecimal("1000.00"));
        request.setPrazoMeses(12);

        EmprestimoResponseDTO responseDTO = EmprestimoResponseDTO.builder()
                .valorSolicitado(new BigDecimal("1000.00"))
                .prazoMeses(12)
                .parcelaMensal(new BigDecimal("100.00"))
                .build();

        when(service.calcular(any())).thenReturn(responseDTO);

        Response response = resource.simular(request);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
    }

    @Test
    @DisplayName("Deve retornar 201 na contratação de empréstimo")
    void deveContratarEmprestimoComSucesso() {
        EmprestimoService service = mock(EmprestimoService.class);
        EmprestimoResource resource = new EmprestimoResource(service);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(2L);
        request.setValorSolicitado(new BigDecimal("2000.00"));
        request.setPrazoMeses(24);

        EmprestimoResponseDTO responseDTO = EmprestimoResponseDTO.builder()
                .valorSolicitado(new BigDecimal("2000.00"))
                .prazoMeses(24)
                .parcelaMensal(new BigDecimal("90.00"))
                .build();

        when(service.calcular(any())).thenReturn(responseDTO);

        Response response = resource.contratar(request);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
    }

    @Test
    @DisplayName("Deve retornar 400 se serviço lançar RegraDeNegocioException na simulação")
    void deveRetornar400SimulacaoErroNegocio() {
        EmprestimoService service = mock(EmprestimoService.class);
        EmprestimoResource resource = new EmprestimoResource(service);

        EmprestimoRequestDTO request = new EmprestimoRequestDTO();
        request.setIdProduto(1L);
        request.setValorSolicitado(BigDecimal.ZERO);
        request.setPrazoMeses(0);

        when(service.calcular(any())).thenThrow(new RegraDeNegocioException("Erro de negócio"));

        // Simula tratamento de exceção (adapte conforme o handler do projeto)
        try {
            resource.simular(request);
            fail("Deveria lançar RegraDeNegocioException");
        } catch (RegraDeNegocioException e) {
            assertEquals("Erro de negócio", e.getMessage());
        }
    }
}