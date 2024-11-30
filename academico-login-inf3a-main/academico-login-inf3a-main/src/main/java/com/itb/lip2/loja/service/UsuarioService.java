package com.itb.lip2.loja.service;

import java.util.List;
import java.util.Optional;

import com.itb.lip2.loja.model.*;

public interface UsuarioService {

	Usuario save(Usuario usuario);
	Usuario saveCliente(Cliente cliente);
	Usuario saveFuncionario(Funcionario funcionario);
	List<Usuario> findAll();
	Optional<Usuario> findById(Long id);
	Usuario update(Long id, Usuario usuario) throws Exception;
	Papel savePapel(Papel papel);
	void addPapelToUsuario(Usuario usuario, String nomePapel);
	List<Usuario> findAllClientes();
	Usuario findByUsername(String username);
	
	 Compra finalizarCompra(Long clienteId) throws Exception;
}