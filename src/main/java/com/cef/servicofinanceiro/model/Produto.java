package com.cef.servicofinanceiro.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal taxaJurosAnual;

    @Column(nullable = false)
    private int prazoMaximoMeses;

    // Construtor vazio (requerido pelo JPA)
    public Produto() {
    }

    // Construtor com todos os argumentos
    public Produto(Long id, String nome, BigDecimal taxaJurosAnual, int prazoMaximoMeses) {
        this.id = id;
        this.nome = nome;
        this.taxaJurosAnual = taxaJurosAnual;
        this.prazoMaximoMeses = prazoMaximoMeses;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getTaxaJurosAnual() { return taxaJurosAnual; }
    public void setTaxaJurosAnual(BigDecimal taxaJurosAnual) { this.taxaJurosAnual = taxaJurosAnual; }

    public int getPrazoMaximoMeses() { return prazoMaximoMeses; }
    public void setPrazoMaximoMeses(int prazoMaximoMeses) { this.prazoMaximoMeses = prazoMaximoMeses; }
}