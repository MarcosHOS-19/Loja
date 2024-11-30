package com.itb.lip2.loja.service;

import com.itb.lip2.loja.model.Cliente;

public interface ClienteService {

    Cliente update(Long id, Cliente cliente) throws Exception;
}