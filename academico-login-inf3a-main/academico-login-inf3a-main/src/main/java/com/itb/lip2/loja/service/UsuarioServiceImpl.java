package com.itb.lip2.loja.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.itb.lip2.loja.exceptions.BadRequest;
import com.itb.lip2.loja.exceptions.NotFound;
import com.itb.lip2.loja.model.*;
import com.itb.lip2.loja.repository.CarrinhoRepository;
import com.itb.lip2.loja.repository.ClienteRepository;
import com.itb.lip2.loja.repository.CompraRepository;
import com.itb.lip2.loja.repository.PapelRepository;
import com.itb.lip2.loja.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.itb.lip2.loja.repository.UsuarioRepository;

import javax.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private PapelRepository papelRepository;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Usuario save(Usuario usuario) {
		usuario.setCodStatusUsuario(true);
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}


	// UsuarioServiceImpl.java

	@Override
	public Usuario saveCliente(Cliente cliente) {
	    cliente.setCodStatusUsuario(true);
	    cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
	    cliente.setPapeis(new ArrayList<>());
	    addPapelToUsuario(cliente, "ROLE_CLIENTE");
	    cliente = usuarioRepository.save(cliente);

	    // Criar um novo carrinho e associá-lo ao cliente
	    Carrinho carrinho = new Carrinho();
	    carrinho.setUsuario(cliente); // Associar o cliente ao carrinho
	    cliente.setCarrinho(carrinho); // Associar o carrinho ao cliente
	    carrinhoRepository.save(carrinho); // Salva o carrinho no repositório

	    return cliente;
	}
	
	@Override
	public Usuario saveFuncionario(Funcionario funcionario) {
		funcionario.setCodStatusUsuario(true);
		funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
		funcionario.setPapeis(new ArrayList<>());
		addPapelToUsuario(funcionario, "ROLE_FUNCIONARIO");
		return usuarioRepository.save(funcionario);
	}

	@Override
	public List<Usuario> findAll() {

		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {

		Optional <Usuario> usuario =  usuarioRepository.findById(id);
		return usuario;
	}

	@Override
	@Transactional
	public Usuario update(Long id, Usuario usuario) throws Exception {
		return usuarioRepository.findById(id).map(user ->{
			user.setNome(usuario.getNome());
			user.setDataNascimento(usuario.getDataNascimento());
			return usuarioRepository.save(user);
		}).orElseThrow(()-> new Exception("Usuário não encontrado!"));
	}

	@Override
	public Papel savePapel(Papel papel) {
		return papelRepository.save(papel);
	}

	@Override
	public void addPapelToUsuario(Usuario usuario, String nomePapel) {
		 Papel papel = papelRepository.findByName(nomePapel);
		    if (papel == null) {
		        papel = new Papel();
		        papel.setNomePapel(nomePapel);
		        papel.setCodStatusPapel(true);
		        papelRepository.save(papel);
		    }
		    usuario.getPapeis().add(papel);
		   
	}

	@Override
	public Usuario findByUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username);
		if(usuario == null) {
			throw  new UsernameNotFoundException("Usuário não encontrado no banco de dados");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		usuario.getPapeis().forEach(papel -> {
			authorities.add(new SimpleGrantedAuthority(papel.getNomePapel()));
		});

		return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getSenha(), authorities);
	}
	
	@Override
	public Compra finalizarCompra(Long clienteId) {
	    Cliente cliente = usuarioRepository.findById(clienteId)
	        .filter(u -> u instanceof Cliente)
	        .map(u -> (Cliente) u)
	        .orElseThrow(() -> new NotFound("Cliente não encontrado"));

	    Carrinho carrinho = cliente.getCarrinho();
	    if (carrinho == null || carrinho.getProdutos() == null || carrinho.getProdutos().isEmpty()) {
	        throw new BadRequest("O carrinho está vazio.");
	    }

	    // Obter o valor total do carrinho
	    double total = carrinho.getValorTotal();

	    // Atualizar o estoque
	    for (ProdutoCarrinho produtoCarrinho : carrinho.getProdutos()) {
	        Produto produto = produtoCarrinho.getProduto();
	        if (produto != null) {
	            if (produto.getQuantidade() < produtoCarrinho.getQuantidade()) {
	                throw new BadRequest("Estoque insuficiente para o produto: " + produto.getNome());
	            }
	            produto.setQuantidade(produto.getQuantidade() - produtoCarrinho.getQuantidade());
	            produtoRepository.save(produto); // Atualiza o produto no banco
	        } else {
	            throw new BadRequest("Produto não encontrado no carrinho.");
	        }
	    }

	    // Salvar a compra no banco de dados
	    Compra compra = new Compra();
	    compra.setCliente(cliente);
	    compra.setTotal(total);
	    compra.setDataCompra(LocalDateTime.now());
	    compraRepository.save(compra);

	    // Limpar o carrinho
	    carrinho.getProdutos().clear();
	    carrinho.setValorTotal(0.0); // Resetar o valor total do carrinho
	    carrinhoRepository.save(carrinho); // Salva o carrinho vazio e com valor total resetado

	    return compra; // Retorna a compra finalizada
	}	
	
	 @Override
	    public List<Usuario> findAllClientes() {
	        return usuarioRepository.findByTipoUsuario("Cliente"); // Método que busca clientes
	    }
}