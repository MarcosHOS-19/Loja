package com.itb.lip2.loja.controller;

import com.itb.lip2.loja.dto.CarrinhoDto;
import com.itb.lip2.loja.dto.ProdutoDto;
import com.itb.lip2.loja.exceptions.BadRequest;
import com.itb.lip2.loja.exceptions.NotFound;
import com.itb.lip2.loja.model.Carrinho;
import com.itb.lip2.loja.model.Cliente;
import com.itb.lip2.loja.model.Compra;
import com.itb.lip2.loja.model.Produto;
import com.itb.lip2.loja.model.ProdutoCarrinho;
import com.itb.lip2.loja.model.Usuario;
import com.itb.lip2.loja.service.CarrinhoService;
import com.itb.lip2.loja.service.ClienteService;
import com.itb.lip2.loja.service.ProdutoService;
import com.itb.lip2.loja.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academico/api/v1/cliente")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;

    
    

    public ClienteController(ClienteService clienteService, UsuarioService usuarioService,
			CarrinhoService carrinhoService, ProdutoService produtoService) {
		super();
		this.clienteService = clienteService;
		this.usuarioService = usuarioService;
		this.carrinhoService = carrinhoService;
		this.produtoService = produtoService;
	}

	@PutMapping("/{id}")
    public ResponseEntity<Object>updateCliente(@RequestBody Cliente cliente, @PathVariable(value="id") Long id) {
        try {
            return ResponseEntity.ok().body(clienteService.update(id, cliente));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    
 // ESSA URL VAI VIRAR UM BOTAO

	@PostMapping("/{id}/adicionar")
	public ResponseEntity<?> adicionarProdutoAoCarrinho(@PathVariable Long id, @RequestBody Map<String, Object> produtoData) throws Exception {
	    Cliente cliente = (Cliente) usuarioService.findById(id)
	        .orElseThrow(() -> new NotFound("Cliente não encontrado"));

	    // Verifica se as chaves existem no Map
	    if (!produtoData.containsKey("id") || !produtoData.containsKey("quantidadeDesejada")) {
	        throw new BadRequest("Dados do produto inválidos.");
	    }

	    Long produtoId = Long.valueOf(produtoData.get("id").toString());
	    int quantidadeDesejada = Integer.parseInt(produtoData.get("quantidadeDesejada").toString());

	    Produto produto = produtoService.findById(produtoId)
	        .orElseThrow(() -> new NotFound("Produto não encontrado"));

	    // Verifica se o produto tem estoque suficiente
	    if (produto.getQuantidade() < quantidadeDesejada) {
	        throw new BadRequest("Quantidade desejada não disponível em estoque.");
	    }

	    Carrinho carrinho = cliente.getCarrinho();
	    ProdutoCarrinho produtoCarrinho = new ProdutoCarrinho();
	    produtoCarrinho.setProduto(produto);
	    produtoCarrinho.setQuantidade(quantidadeDesejada);

	    // Adiciona o produto ao carrinho
	    carrinhoService.adicionarProduto(carrinho, produtoCarrinho);
	    
	    // Atualiza a quantidade do produto
	    produto.setQuantidade(produto.getQuantidade() - quantidadeDesejada);
	    produtoService.update(produto.getId(), produto); // Atualiza o produto no banco

	    return ResponseEntity.ok().body("Produto adicionado ao carrinho com sucesso.");
	}
	
	//ESSA URL VAI VIRAR UM BOTAO
	@PostMapping("/{id}/finalizar-compra")
	public ResponseEntity<?> finalizarCompra(@PathVariable Long id) throws Exception {
	    Cliente cliente = (Cliente) usuarioService.findById(id)
	        .orElseThrow(() -> new NotFound("Cliente não encontrado"));

	    Carrinho carrinho = cliente.getCarrinho();
	    if (carrinho.getProdutos().isEmpty()) {
	        throw new BadRequest("O carrinho está vazio.");
	    }

	    // Cria uma nova compra
	    Compra compra = new Compra();
	    compra.setCliente(cliente);
	    compra.setDataCompra(LocalDateTime.now());

	    double valorTotal = 0.0; // Inicializa o valor total

	    // Adiciona os produtos do carrinho à compra
	    for (ProdutoCarrinho produtoCarrinho : carrinho.getProdutos()) {
	        Produto produto = produtoCarrinho.getProduto();
	        int quantidade = produtoCarrinho.getQuantidade();

	        // Verifica se há estoque suficiente
	        if (produto.getQuantidade() < quantidade) {
	            throw new BadRequest("Quantidade solicitada não disponível para o produto: " + produto.getNome());
	        }

	        ProdutoCarrinho novoProdutoCarrinho = new ProdutoCarrinho();
	        novoProdutoCarrinho.setProduto(produto);
	        novoProdutoCarrinho.setQuantidade(quantidade);
	        novoProdutoCarrinho.setCompra(compra);
	        compra.getProdutos().add(novoProdutoCarrinho);

	        // Calcula o valor total
	        valorTotal += produto.getPreco() * quantidade; // Usando o preço do produto
	    }

	    compra.setTotal(valorTotal); // Define o total da compra

	    // Salva a compra usando o CarrinhoService
	    carrinhoService.finalizarCompra(compra);

	    // Reseta o carrinho
	    carrinho.getProdutos().clear(); // Limpa os produtos do carrinho
	    carrinho.setValorTotal(0.0); // Reseta o valor total do carrinho

	    // Salva as alterações no carrinho
	    carrinhoService.atualizarCarrinho(carrinho); // Certifique-se de ter um método para atualizar o carrinho

	    return ResponseEntity.ok().body("Compra realizada com sucesso. O pagamento será feito em breve.");
	}	
	
	//ESSA URL VAI VIRAR UM BOTAO
	@PatchMapping("/{id}/remover-quantidade/{produtoId}")
	public ResponseEntity<?> removerQuantidadeDoProduto(@PathVariable Long id, @PathVariable Long produtoId, @RequestParam int quantidade) throws Exception {
	    Cliente cliente = (Cliente) usuarioService.findById(id)
	        .orElseThrow(() -> new NotFound("Cliente não encontrado"));

	    Carrinho carrinho = cliente.getCarrinho();
	    ProdutoCarrinho produtoCarrinho = carrinhoService.findProdutoInCarrinho(carrinho, produtoId)
	        .orElseThrow(() -> new NotFound("Produto não encontrado no carrinho"));

	    // Verifica se a quantidade a ser removida é válida
	    if (quantidade <= 0) {
	        return ResponseEntity.badRequest().body("A quantidade deve ser maior que zero.");
	    }

	    // Verifica se a quantidade a ser removida não excede a quantidade atual
	    if (quantidade > produtoCarrinho.getQuantidade()) {
	        return ResponseEntity.badRequest().body("Quantidade a ser removida excede a quantidade atual no carrinho.");
	    }

	    // Remove a quantidade do produto
	    produtoCarrinho.setQuantidade(produtoCarrinho.getQuantidade() - quantidade);

	    // Se a quantidade chegar a zero, remove o produto do carrinho
	    if (produtoCarrinho.getQuantidade() == 0) {
	        carrinho.getProdutos().remove(produtoCarrinho);
	    }

	    // Atualiza o carrinho
	    carrinhoService.atualizarCarrinho(carrinho); // Método para atualizar o carrinho no repositório

	    return ResponseEntity.ok().body("Quantidade removida do produto com sucesso.");
	}
	
	//AQUI VAI MOSTRAR O CARRINHO DO CLIENTE EM TEMPO REAL
	@GetMapping("/{id}/carrinho")
	public ResponseEntity<CarrinhoDto> getCarrinho(@PathVariable Long id) {
	    // Busca o cliente pelo ID
	    Usuario usuario = usuarioService.findById(id)
	        .orElseThrow(() -> new NotFound("Cliente não encontrado"));

	    // Verifica se o usuário é um cliente
	    if (!(usuario instanceof Cliente)) {
	        throw new BadRequest("Usuário não é um cliente.");
	    }

	    Cliente cliente = (Cliente) usuario; // Cast seguro
	    Carrinho carrinho = cliente.getCarrinho();

	    // Verifica se o carrinho é nulo ou não tem produtos
	    if (carrinho == null || (carrinho.getProdutos() == null || carrinho.getProdutos().isEmpty())) {
	        throw new BadRequest("Carrinho vazio ou não encontrado.");
	    }

	    double valorTotal = 0.0; // Inicializa o valor total
	    List<ProdutoDto> produtosDTO = new ArrayList<>(); // Lista para armazenar os produtos

	    // Itera sobre os produtos do carrinho e calcula o valor total
		for (ProdutoCarrinho produtoCarrinho : carrinho.getProdutos()) {
			Produto produto = produtoCarrinho.getProduto();
			double valorProduto = produto.getPreco() * produtoCarrinho.getQuantidade();
			valorTotal += valorProduto;

			ProdutoDto dto = new ProdutoDto();
			dto.setId(produto.getId());
			dto.setNome(produto.getNome());
			dto.setQuantidadeDesejada(produtoCarrinho.getQuantidade());
			dto.setValor(valorProduto);
			dto.setPrecoUnitario(produto.getPreco()); // Aqui você define o preço unitário

			// Adiciona a imagem do produto ao DTO
			String imagemPath = produto.getImagem();
			if (imagemPath != null && !imagemPath.isEmpty()) {
				dto.setImagem(imagemPath);
			}

			produtosDTO.add(dto);
		}

	    // Cria um DTO para o carrinho com o valor total e a lista de produtos
	    CarrinhoDto carrinhoResponse = new CarrinhoDto();
	    carrinhoResponse.setId(carrinho.getId());
	    carrinhoResponse.setProdutos(produtosDTO);

	    // Retorna o carrinho com os produtos e o valor total
	    return ResponseEntity.ok(carrinhoResponse);
	}
}