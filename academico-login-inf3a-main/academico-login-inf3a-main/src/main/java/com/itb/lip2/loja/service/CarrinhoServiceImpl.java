package com.itb.lip2.loja.service;


import com.itb.lip2.loja.model.Carrinho;
import com.itb.lip2.loja.model.Compra;
import com.itb.lip2.loja.model.ProdutoCarrinho;
import com.itb.lip2.loja.model.Usuario;
import com.itb.lip2.loja.repository.CarrinhoRepository;
import com.itb.lip2.loja.repository.CompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;
    @Autowired
    private CompraRepository compraRepository; // Injetar o repositório de compras


    @Override
    public Carrinho criarCarrinho(Usuario usuario) { // Alterado para Usuario
        Carrinho carrinho = new Carrinho();
        carrinho.setUsuario(usuario); // Ajustado para usar Usuario
        return carrinhoRepository.save(carrinho);
    }

    @Override
    public void adicionarProduto(Carrinho carrinho, ProdutoCarrinho produtoCarrinho) {
        if (carrinho == null || produtoCarrinho == null) {
            throw new IllegalArgumentException("Carrinho ou produto não podem ser nulos");
        }
        
        Optional<ProdutoCarrinho> produtoExistente = findProdutoInCarrinho(carrinho, produtoCarrinho.getProduto().getId());
        if (produtoExistente.isPresent()) {
            // Atualiza a quantidade se o produto já existir
            produtoExistente.get().setQuantidade(produtoExistente.get().getQuantidade() + produtoCarrinho.getQuantidade());
        } else {
            carrinho.adicionarProduto(produtoCarrinho.getProduto(), produtoCarrinho.getQuantidade());
        }
        
        carrinhoRepository.save(carrinho); // Salva as alterações no carrinho
    }

    @Override
    public double calcularValorTotal(Carrinho carrinho) {
        return carrinho.getValorTotal(); // Retorna o valor total do carrinho
    }
    
    public void atualizarCarrinho(Carrinho carrinho) {
        carrinhoRepository.save(carrinho);
    }

	@Override
	public Optional<ProdutoCarrinho> findProdutoInCarrinho(Carrinho carrinho, Long produtoId) {
		 return carrinho.getProdutos().stream()
			        .filter(produtoCarrinho -> produtoCarrinho.getProduto().getId().equals(produtoId))
			        .findFirst();
	}
	
	public List<Carrinho> listarCarrinhos() {
        return carrinhoRepository.findAll(); // Retorna todos os carrinhos
    }

	
	 public void resetarCarrinhos() {
	        List<Carrinho> carrinhos = carrinhoRepository.findAll();
	        for (Carrinho carrinho : carrinhos) {
	            carrinho.getProdutos().clear(); // Limpa os produtos do carrinho
	            carrinhoRepository.save(carrinho); // Salva o carrinho vazio
	        }
	    }
	 public void finalizarCompra(Compra compra) {
	        // Salva a compra no repositório
	        compraRepository.save(compra);
	    }
}