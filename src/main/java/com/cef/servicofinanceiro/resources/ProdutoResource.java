package com.cef.servicofinanceiro.resources;

import com.cef.servicofinanceiro.dto.ProdutoRequestDTO;
import com.cef.servicofinanceiro.dto.ProdutoResponseDTO;
import com.cef.servicofinanceiro.service.ProdutoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Operações para gerenciar os produtos de empréstimo.")
public class ProdutoResource {

    private final ProdutoService produtoService;

    @Inject
    public ProdutoResource(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @POST
    @Operation(
            summary = "Cria um novo produto",
            description = "Registra um novo produto de empréstimo no sistema com base nos dados fornecidos."
    )
    @APIResponse(
            responseCode = "201",
            description = "Produto criado com sucesso.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProdutoResponseDTO.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos para a criação do produto."
    )
    public Response criar(
            @RequestBody(
                    description = "Dados do novo produto a ser criado.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProdutoRequestDTO.class))
            )
            @Valid ProdutoRequestDTO requestDTO) {

        ProdutoResponseDTO produtoCriado = produtoService.criar(requestDTO);
        URI location = UriBuilder.fromResource(ProdutoResource.class).path("/{id}").build(produtoCriado.getId());
        return Response.created(location).entity(produtoCriado).build();
    }

    @GET
    @Operation(
            summary = "Lista todos os produtos",
            description = "Retorna uma lista de todos os produtos de empréstimo cadastrados no sistema."
    )
    @APIResponse(
            responseCode = "200",
            description = "Lista de produtos retornada com sucesso.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoResponseDTO.class))
    )
    public Response listarTodos() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodos();
        return Response.ok(produtos).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
            summary = "Busca um produto por ID",
            description = "Retorna os detalhes de um produto específico com base no seu identificador único."
    )
    @APIResponse(
            responseCode = "200",
            description = "Produto encontrado e retornado com sucesso.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProdutoResponseDTO.class))
    )
    @APIResponse(
            responseCode = "404",
            description = "Nenhum produto encontrado para o ID fornecido."
    )
    public Response buscarPorId(
            @Parameter(description = "ID do produto a ser buscado.", required = true, example = "1")
            @PathParam("id") Long id) {

        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return Response.ok(produto).build();
    }
}