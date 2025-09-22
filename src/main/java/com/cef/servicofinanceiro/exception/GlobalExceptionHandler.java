package com.cef.servicofinanceiro.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.Map;

public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Response handleNotFound(EntityNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("erro", ex.getMessage()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleBusinessRule(RegraDeNegocioException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("erro", ex.getMessage()))
                .build();
    }
}