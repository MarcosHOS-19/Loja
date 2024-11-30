package com.itb.lip2.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itb.lip2.loja.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);
    @Query(value = "SELECT * FROM usuarios u WHERE u.email=?", nativeQuery = true)
    Usuario findByUsername(String username);
	List<Usuario> findByTipoUsuario(String string);


}