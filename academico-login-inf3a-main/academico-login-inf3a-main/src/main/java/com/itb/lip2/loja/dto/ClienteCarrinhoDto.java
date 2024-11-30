package com.itb.lip2.loja.dto;

public class ClienteCarrinhoDto {
    private String nome;
    private String rm;
    private Long id; // ID do cliente

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}