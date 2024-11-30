package com.itb.lip2.loja.dto;

public class ProdutoDto {
    
    private Long id;
    private String nome; // Nome do produto
    private int quantidadeDesejada; // Quantidade desejada
    private double valor; // Valor do produto, agora como double
    private String imagem; // Novo atributo para armazenar a imagem
    private double precoUnitario;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeDesejada() {
        return quantidadeDesejada;
    }

    public void setQuantidadeDesejada(int quantidadeDesejada) {
        this.quantidadeDesejada = quantidadeDesejada;
    }

    public double getValor() {
        return valor; // Retorna o valor como double
    }

    public void setValor(double valor) {
        this.valor = valor; // Define o valor como double
    }

    public String getImagem() {
        return imagem; // Retorna o caminho da imagem
    }

    public void setImagem(String imagem) {
        this.imagem = imagem; // Define o caminho da imagem
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}