package com.itb.lip2.loja.service;

import java.util.List;
import java.util.Optional;

//CarrinhoService.java

import com.itb.lip2.loja.model.Carrinho;
import com.itb.lip2.loja.model.Compra;
import com.itb.lip2.loja.model.ProdutoCarrinho;
import com.itb.lip2.loja.model.Usuario;

public interface CarrinhoService {
    void adicionarProduto(Carrinho carrinho, ProdutoCarrinho produtoCarrinho);
    double calcularValorTotal(Carrinho carrinho);
    Carrinho criarCarrinho(Usuario usuario);
    Optional<ProdutoCarrinho> findProdutoInCarrinho(Carrinho carrinho, Long produtoId);
    void atualizarCarrinho(Carrinho carrinho);
    List<Carrinho> listarCarrinhos();
	void resetarCarrinhos();
	void finalizarCompra(Compra compra);
}