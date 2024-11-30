package com.itb.lip2.loja.service;

import com.itb.lip2.loja.exceptions.NotFound;
import com.itb.lip2.loja.model.Produto;
import com.itb.lip2.loja.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    @Transactional
 // ProdutoService.java
    public Produto update(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = produtoRepository.findById(id)
            .orElseThrow(() -> new NotFound("Produto não encontrado"));

        // Atualiza os campos do produto existente
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        // Adicione outros campos conforme necessário

        return produtoRepository.save(produtoExistente); // Salva as alterações
    }
    @Override
    @Transactional
    public void delete(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Produto não encontrado"));
        produtoRepository.delete(produto); // Remove o produto do repositório
    }
}