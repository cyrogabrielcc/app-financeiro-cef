package com.cef.servicofinanceiro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "EmprestimoRequest", description = "Representa os dados de entrada para uma simulação ou contratação de empréstimo.")
public class EmprestimoRequestDTO {

    @Schema(description = "Identificador único do produto de empréstimo desejado.", example = "1", required = true)
    @NotNull(message = "O ID do produto é obrigatório.")
    private Long idProduto;

    @Schema(description = "Valor monetário que o cliente deseja solicitar.", example = "5000.00", required = true)
    @NotNull(message = "O valor solicitado é obrigatório.")
    @Positive(message = "O valor solicitado deve ser positivo.")
    private BigDecimal valorSolicitado;

    @Schema(description = "Número de meses para o pagamento do empréstimo.", example = "12", required = true)
    @NotNull(message = "O prazo em meses é obrigatório.")
    @Positive(message = "O prazo deve ser um número positivo de meses.")
    private int prazoMeses;


    // Getters e Setters
    public Long getIdProduto() { return idProduto; }
    public void setIdProduto(Long idProduto) { this.idProduto = idProduto; }

    public BigDecimal getValorSolicitado() { return valorSolicitado; }
    public void setValorSolicitado(BigDecimal valorSolicitado) { this.valorSolicitado = valorSolicitado; }

    public int getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(int prazoMeses) { this.prazoMeses = prazoMeses; }
}