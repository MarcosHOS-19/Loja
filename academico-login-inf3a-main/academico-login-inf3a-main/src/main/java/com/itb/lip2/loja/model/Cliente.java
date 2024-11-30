package com.itb.lip2.loja.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Collection;

@Entity
@DiscriminatorValue(value = "Cliente")
public class Cliente extends Usuario {

    private String rm;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL) // Alterado para usuario
    private Carrinho carrinho;

    public Cliente() {
        // Construtor padrão
    }


    public Cliente(Long id, String nome, String email, String senha, String tipoUsuario, Collection<Papel> papeis) {
        super(id, nome, email, senha, tipoUsuario, papeis);
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}