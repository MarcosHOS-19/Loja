package com.itb.lip2.loja.dto;

import java.util.List;

public class CarrinhoDto {
	 private Long id;
	 private List<ProdutoDto> produtos;
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
	/**
	 * @return the produtos
	 */
	public List<ProdutoDto> getProdutos() {
		return produtos;
	}
	/**
	 * @param produtos the produtos to set
	 */
	public void setProdutos(List<ProdutoDto> produtos) {
		this.produtos = produtos;
	}
	 
	 
}
