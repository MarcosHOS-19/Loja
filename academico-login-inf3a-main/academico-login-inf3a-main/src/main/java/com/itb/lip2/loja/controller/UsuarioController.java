package com.itb.lip2.loja.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.itb.lip2.loja.exceptions.NotFound;
import com.itb.lip2.loja.model.Cliente;
import com.itb.lip2.loja.model.Funcionario;
import com.itb.lip2.loja.model.Produto;
import com.itb.lip2.loja.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource; // Certifique-se de que esta importação está presente
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.itb.lip2.loja.model.Usuario;
import com.itb.lip2.loja.service.UsuarioService;


@RestController
@RequestMapping("/academico/api/v1")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private ProdutoService produtoService;

	@GetMapping("/users")
	public ResponseEntity<List<Usuario>> getUsers() {

		return ResponseEntity.ok().body(usuarioService.findAll());
	}

	@GetMapping("/users/produtos")
	public ResponseEntity<List<Produto>> getProdutos() {
		return ResponseEntity.ok().body(produtoService.findAll());
	}

	@PostMapping("/users")
	public ResponseEntity<Usuario> saveUser(@RequestBody Usuario usuario) {

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/loja/api/v1/users").toUriString());
		return ResponseEntity.created(uri).body(usuarioService.save(usuario));

	}


	@PostMapping("/users/cliente")
	public ResponseEntity<Usuario> saveCliente(@RequestBody Cliente cliente) {

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/loja/api/v1/users").toUriString());
		return ResponseEntity.created(uri).body(usuarioService.saveCliente(cliente));

	}

	@PostMapping("/users/funcionario")
	public ResponseEntity<Usuario> saveFuncionario(@RequestBody Funcionario funcionario) {

		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/loja/api/v1/users").toUriString());
		return ResponseEntity.created(uri).body(usuarioService.saveFuncionario(funcionario));

	}
	@GetMapping("/users/{id}")
	public ResponseEntity<Object> findUserById(@PathVariable(value = "id") Long id) {
		//return ResponseEntity.ok().body(usuarioService.findById(id).get());
		try{
			return ResponseEntity.ok().body(usuarioService.findById(id).get());
		}catch (Exception e){
			throw new NotFound("Usuario nao encontrado " + id);
		}
	}
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id, @RequestBody Usuario usuario) {
		try{
			return ResponseEntity.ok().body(usuarioService.update(id, usuario));
		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/users/imagens/{fileName:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
		try {
			// Caminho completo para o diretório onde as imagens estão salvas
			Path path = Paths.get("C:\\Users\\marco\\Documents\\work\\rou" + fileName);
			Resource resource = new UrlResource(path.toUri());
			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.ok().body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			return ResponseEntity.notFound().build();
		}
	}
}