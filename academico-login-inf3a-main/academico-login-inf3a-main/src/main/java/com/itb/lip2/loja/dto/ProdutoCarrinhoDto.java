package com.itb.lip2.loja.dto;

public class ProdutoCarrinhoDto {
	private Long id;
    private String nome;
    private int quantidade;
    private double valor;
    private String imagem;
    // Getters e Setters
    
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
        return nome;
    }
	public void setNome(String nome) {
        this.nome = nome;
    }
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    public String getImagem() {
        return imagem; // Retorna o caminho da imagem
    }

    public void setImagem(String imagem) {
        this.imagem = imagem; // Define o caminho da imagem
    }
}