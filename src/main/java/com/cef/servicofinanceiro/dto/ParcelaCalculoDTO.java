package com.cef.servicofinanceiro.dto;

import java.math.BigDecimal;


public class ParcelaCalculoDTO {
    private int mes;
    private BigDecimal saldoDevedorInicial; // NOVO CAMPO
    private BigDecimal juros;
    private BigDecimal amortizacao;
    private BigDecimal saldoDevedorFinal; // RENOMEADO (era saldoDevedor)

    // O campo valorParcela foi removido daqui, pois ele agora está no nível superior do JSON

    private ParcelaCalculoDTO(int mes, BigDecimal saldoDevedorInicial, BigDecimal juros, BigDecimal amortizacao, BigDecimal saldoDevedorFinal) {
        this.mes = mes;
        this.saldoDevedorInicial = saldoDevedorInicial;
        this.juros = juros;
        this.amortizacao = amortizacao;
        this.saldoDevedorFinal = saldoDevedorFinal;
    }

    // Getters
    public int getMes() { return mes; }
    public BigDecimal getSaldoDevedorInicial() { return saldoDevedorInicial; }
    public BigDecimal getJuros() { return juros; }
    public BigDecimal getAmortizacao() { return amortizacao; }
    public BigDecimal getSaldoDevedorFinal() { return saldoDevedorFinal; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int mes;
        private BigDecimal saldoDevedorInicial;
        private BigDecimal juros;
        private BigDecimal amortizacao;
        private BigDecimal saldoDevedorFinal;

        public Builder mes(int mes) { this.mes = mes; return this; }
        public Builder saldoDevedorInicial(BigDecimal saldoDevedorInicial) { this.saldoDevedorInicial = saldoDevedorInicial; return this; }
        public Builder juros(BigDecimal juros) { this.juros = juros; return this; }
        public Builder amortizacao(BigDecimal amortizacao) { this.amortizacao = amortizacao; return this; }
        public Builder saldoDevedorFinal(BigDecimal saldoDevedorFinal) { this.saldoDevedorFinal = saldoDevedorFinal; return this; }

        public ParcelaCalculoDTO build() {
            return new ParcelaCalculoDTO(mes, saldoDevedorInicial, juros, amortizacao, saldoDevedorFinal);
        }
    }
}