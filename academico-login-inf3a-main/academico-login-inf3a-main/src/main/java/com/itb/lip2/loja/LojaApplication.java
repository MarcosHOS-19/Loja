package com.itb.lip2.loja;

import com.itb.lip2.loja.model.Papel;
import com.itb.lip2.loja.repository.PapelRepository;
import com.itb.lip2.loja.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class
LojaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaApplication.class, args);

	}


	@Bean
	CommandLineRunner run(UsuarioService usuarioService, PapelRepository papelRepository){
		return args -> {
			if(papelRepository.findAll().size() == 0) {
				usuarioService.savePapel(new Papel(null, "ROLE_CLIENTE"));
				usuarioService.savePapel(new Papel(null, "ROLE_FUNCIONARIO"));
				usuarioService.savePapel(new Papel(null, "ROLE_ADMIN"));
			}else {
				System.out.println("Papeis já criados no banco de dados!");
			}
		};
	}

}