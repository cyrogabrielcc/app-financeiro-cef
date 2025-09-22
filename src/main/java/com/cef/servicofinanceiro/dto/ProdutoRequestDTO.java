package com.cef.servicofinanceiro.dto;

import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public class ProdutoRequestDTO {
    @Schema(description = "Nome comercial do produto de empréstimo.", example = "Crédito Pessoal Plus", required = true)
    @NotBlank(message = "O nome do produto não pode ser vazio.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @Schema(description = "Taxa de juros anual do produto, em porcentagem (%).", example = "21.5", required = true)
    @NotNull(message = "A taxa de juros anual é obrigatória.")
    @Positive(message = "A taxa de juros deve ser um valor positivo.")
    private BigDecimal taxaJurosAnual;

    @Schema(description = "Prazo máximo de pagamento em meses.", example = "36", required = true)
    @NotNull(message = "O prazo máximo é obrigatório.")
    @Min(value = 1, message = "O prazo mínimo deve ser de 1 mês.")
    private int prazoMaximoMeses;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getTaxaJurosAnual() { return taxaJurosAnual; }
    public void setTaxaJurosAnual(BigDecimal taxaJurosAnual) { this.taxaJurosAnual = taxaJurosAnual; }

    public int getPrazoMaximoMeses() { return prazoMaximoMeses; }
    public void setPrazoMaximoMeses(int prazoMaximoMeses) { this.prazoMaximoMeses = prazoMaximoMeses; }
}