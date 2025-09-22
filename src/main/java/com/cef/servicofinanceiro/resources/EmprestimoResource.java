package com.cef.servicofinanceiro.resources;

import com.cef.servicofinanceiro.dto.EmprestimoRequestDTO;
import com.cef.servicofinanceiro.dto.EmprestimoResponseDTO;
import com.cef.servicofinanceiro.service.EmprestimoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/emprestimos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Operações de Empréstimo", description = "Endpoints para simular e contratar empréstimos.")
public class EmprestimoResource {

    private final EmprestimoService emprestimoService;

    @Inject
    public EmprestimoResource(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @POST
    @Path("/simulacao")
    @Operation(
            summary = "Simula um empréstimo",
            description = "Calcula e retorna todos os detalhes de um empréstimo com base no produto, valor e prazo solicitados. Usa o sistema de amortização Price."
    )
    @APIResponse(
            responseCode = "200",
            description = "Simulação calculada com sucesso.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = EmprestimoResponseDTO.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Dados inválidos na requisição. Pode ser um erro de validação (campos nulos/negativos) ou uma regra de negócio (ex: prazo maior que o permitido)."
    )
    @APIResponse(
            responseCode = "404",
            description = "O produto de empréstimo com o ID informado não foi encontrado."
    )
    public Response simular(
            @RequestBody(
                    description = "Dados necessários para realizar a simulação.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmprestimoRequestDTO.class))
            )
            @Valid EmprestimoRequestDTO requestDTO) {

        var simulacao = emprestimoService.calcular(requestDTO);
        return Response.ok(simulacao).build();
    }

    @POST
    @Path("/contratacao")
    @Operation(
            summary = "Contrata um empréstimo",
            description = "Realiza a contratação de um empréstimo. (Neste projeto, a lógica é idêntica à simulação, mas em um cenário real, persistiria o contrato no banco)."
    )
    @APIResponse(
            responseCode = "201",
            description = "Empréstimo contratado com sucesso.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = EmprestimoResponseDTO.class))
    )
    @APIResponse(responseCode = "400", description = "Dados inválidos na requisição.")
    @APIResponse(responseCode = "404", description = "Produto não encontrado.")
    public Response contratar(
            @RequestBody(
                    description = "Dados necessários para realizar a contratação.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmprestimoRequestDTO.class))
            )
            @Valid EmprestimoRequestDTO requestDTO) {

        var contrato = emprestimoService.calcular(requestDTO);
        return Response.status(Response.Status.CREATED).entity(contrato).build();
    }
}