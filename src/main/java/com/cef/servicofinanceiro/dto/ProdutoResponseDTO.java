package com.cef.servicofinanceiro.dto;


import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ProdutoResponse", description = "Representa os dados de um produto de empréstimo.")
public class ProdutoResponseDTO {
    @Schema(description = "Identificador único do produto.", readOnly = true)
    private Long id;

    @Schema(description = "Nome comercial do produto de empréstimo.")
    private String nome;

    @Schema(description = "Taxa de juros anual do produto, em porcentagem (%).")
    private BigDecimal taxaJurosAnual;

    @Schema(description = "Prazo máximo de pagamento em meses.")
    private int prazoMaximoMeses;

    private ProdutoResponseDTO(Long id, String nome, BigDecimal taxaJurosAnual, int prazoMaximoMeses) {
        this.id = id;
        this.nome = nome;
        this.taxaJurosAnual = taxaJurosAnual;
        this.prazoMaximoMeses = prazoMaximoMeses;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getTaxaJurosAnual() { return taxaJurosAnual; }
    public int getPrazoMaximoMeses() { return prazoMaximoMeses; }

    public static Builder builder() {
        return new Builder();
    }

    // Implementação manual do Padrão Builder
    public static class Builder {
        private Long id;
        private String nome;
        private BigDecimal taxaJurosAnual;
        private int prazoMaximoMeses;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nome(String nome) { this.nome = nome; return this; }
        public Builder taxaJurosAnual(BigDecimal taxaJurosAnual) { this.taxaJurosAnual = taxaJurosAnual; return this; }
        public Builder prazoMaximoMeses(int prazoMaximoMeses) { this.prazoMaximoMeses = prazoMaximoMeses; return this; }

        public ProdutoResponseDTO build() {
            return new ProdutoResponseDTO(id, nome, taxaJurosAnual, prazoMaximoMeses);
        }
    }
}