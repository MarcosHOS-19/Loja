package com.itb.lip2.loja.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.itb.lip2.loja.dto.ClienteCarrinhoDto;
import com.itb.lip2.loja.dto.CompraDto;
import com.itb.lip2.loja.dto.ProdutoCarrinhoDto;
import com.itb.lip2.loja.dto.UsuarioDto;
import com.itb.lip2.loja.exceptions.NotFound;
import com.itb.lip2.loja.model.Cliente;
import com.itb.lip2.loja.model.Produto;
import com.itb.lip2.loja.model.ProdutoCarrinho;
import com.itb.lip2.loja.model.Usuario;
import com.itb.lip2.loja.model.Compra;
import com.itb.lip2.loja.service.ProdutoService;
import com.itb.lip2.loja.service.UsuarioService;
import com.itb.lip2.loja.repository.CompraRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/academico/api/v1")
public class FuncionarioController {

    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CompraRepository compraRepository; // Adicione o repositório de compras

    @GetMapping("/funcionario/produto")
    public ResponseEntity<List<Produto>> getProdutos() {
        return ResponseEntity.ok().body(produtoService.findAll());
    }

    @PostMapping("/funcionario/produto")
    public ResponseEntity<Produto> saveProduto(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("preco") double preco,
            @RequestParam("quantidade") int quantidade,
            @RequestParam("imagem") MultipartFile imagem) {
        
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQuantidade(quantidade);
        
        if (imagem != null && !imagem.isEmpty()) {
            try {
                // Define o diretório onde a imagem será salva
                String uploadDir = "C:\\Users\\marco\\Documents\\work\\rou\\"; // Altere para o seu diretório
                String fileName = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                File file = new File(uploadDir + fileName);
                imagem.transferTo(file); // Salva a imagem no diretório
                
                produto.setImagem(fileName); // Armazena o nome do arquivo no objeto Produto
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retorna 500 em caso de erro
            }
        } else {
            return ResponseEntity.badRequest().body(null); // Retorna 400 se a imagem não for fornecida
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/academico/api/v1/funcionario").toUriString());
        return ResponseEntity.created(uri).body(produtoService.save(produto));
    }

    @GetMapping("/funcionario/{id}")
    public ResponseEntity<Object> findProdutoById(@PathVariable(value = "id") Long id) {
        try {
            return ResponseEntity.ok().body(produtoService.findById(id).get());
        } catch (Exception e) {
            throw new NotFound("Produto não encontrado " + id);
        }
    }

    @PutMapping("/funcionario/{id}")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id, @RequestBody Produto produto) {
        try {
            return ResponseEntity.ok().body(produtoService.update(id, produto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/funcionario/produto/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable(value = "id") Long id) {
        try {
            produtoService.delete(id); // Chama o serviço para deletar o produto
            return ResponseEntity.noContent().build(); // Retorna 204 No Content se a exclusão for bem-sucedida
        } catch (NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 se o produto não for encontrado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna 500 em caso de erro
        }
    }


    // Método para listar clientes que compraram no dia atual
    @GetMapping("/funcionario/clientes/compras-hoje")
    public ResponseEntity<List<ClienteCarrinhoDto>> getClientesComComprasHoje() {
    	LocalDate hoje = LocalDate.now();
    	LocalDateTime inicioDoDia = hoje.atStartOfDay(); // 00:00:00
    	LocalDateTime fimDoDia = hoje.atTime(23, 59, 59); // 23:59:59
    	List<Compra> comprasDoDia = compraRepository.findByDataCompraBetween(inicioDoDia, fimDoDia);
        
    	List<ClienteCarrinhoDto> clientesCarrinho = new ArrayList<>();
        for (Compra compra : comprasDoDia) {
            Cliente cliente = (Cliente) compra.getCliente();
            ClienteCarrinhoDto dto = new ClienteCarrinhoDto();
            dto.setNome(cliente.getNome());
            dto.setRm(cliente.getRm());
            dto.setId(cliente.getId());
            clientesCarrinho.add(dto);
        }

        return ResponseEntity.ok(clientesCarrinho);
    }

    // Método para obter o carrinho de um cliente específico
    @GetMapping("/funcionario/compras/{clienteId}")
    public ResponseEntity<List<CompraDto>> getComprasDoCliente(@PathVariable Long clienteId) {
        Cliente cliente = (Cliente) usuarioService.findById(clienteId)
            .orElseThrow(() -> new NotFound("Cliente não encontrado"));

        List<Compra> compras = compraRepository.findByCliente(cliente);

        List<CompraDto> comprasDto = new ArrayList<>();
        for (Compra compra : compras) {
            CompraDto dto = new CompraDto();
            dto.setId(compra.getId());
            dto.setTotal(compra.getTotal());
            dto.setDataCompra(compra.getDataCompra());

            // Preencher a lista de produtos
            List<ProdutoCarrinhoDto> produtosDto = new ArrayList<>();
            for (ProdutoCarrinho produtoCarrinho : compra.getProdutos()) { // Supondo que você tenha uma relação de produtos na compra
            	ProdutoCarrinhoDto produtoDto = new ProdutoCarrinhoDto();
            	produtoDto.setId(produtoCarrinho.getProduto().getId());
                produtoDto.setNome(produtoCarrinho.getProduto().getNome());
                produtoDto.setQuantidade(produtoCarrinho.getQuantidade());
                produtoDto.setValor(produtoCarrinho.getProduto().getPreco());

                // Recuperar a imagem do produto
                String imagemPath = produtoCarrinho.getProduto().getImagem();
                if (imagemPath != null && !imagemPath.isEmpty()) {
                    produtoDto.setImagem(imagemPath);
                }

                produtosDto.add(produtoDto);
            }
            dto.setProdutos(produtosDto); // Adiciona a lista de produtos ao DTO da compra

            comprasDto.add(dto);
        }

        return ResponseEntity.ok(comprasDto);
    }
    
    @GetMapping("/funcionario/clientes")
    public ResponseEntity<List<UsuarioDto>> listarClientes() {
        List<Usuario> clientes = usuarioService.findAllClientes();
        List<UsuarioDto> clientesDto = new ArrayList<>();

        for (Usuario cliente : clientes) {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(cliente.getId());
            dto.setNome(cliente.getNome());
            dto.setEmail(cliente.getEmail());
            // Adicione outros campos conforme necessário
            clientesDto.add(dto);
        }

        return ResponseEntity.ok(clientesDto);
    }
}