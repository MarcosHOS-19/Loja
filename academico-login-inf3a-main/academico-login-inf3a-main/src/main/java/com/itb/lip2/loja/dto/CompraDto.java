package com.itb.lip2.loja.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CompraDto {
    private Long id;
    private double total;
    private LocalDateTime dataCompra;
    private List<ProdutoCarrinhoDto> produtos; // Nova lista de produtos

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public List<ProdutoCarrinhoDto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoCarrinhoDto> produtos) {
        this.produtos = produtos;
    }
}