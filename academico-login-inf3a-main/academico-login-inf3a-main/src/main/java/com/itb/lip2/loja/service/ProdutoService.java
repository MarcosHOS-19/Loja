package com.itb.lip2.loja.service;

import com.itb.lip2.loja.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    Produto save(Produto produto);

    List<Produto> findAll();

    Optional<Produto> findById(Long id);

    Produto update(Long id, Produto produto) throws Exception;

    void delete(Long id);
}