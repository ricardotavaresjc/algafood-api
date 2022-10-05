package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@SpringBootTest
class CadastroDeCozinhaIntegrationTest {
	
	@Autowired
	private CadastroCozinhaService service;

	//Happy path - caminho feliz
	@Test
	public void testarCadasgroDeCozinhaComSucesso() {
		//cenario
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		//ação
		novaCozinha = service.salvar(novaCozinha);
		
		//verificação
		//Biblioteca que fornece validaçoes em linguagem fluentes
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	public void testarCadastroCozinhaSemNome() {
		//cenario
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		//acao
		ConstraintViolationException erroEsperado = 
				Assertions.assertThrows(ConstraintViolationException.class, ()->{
					service.salvar(novaCozinha);
				});
		//verificação
		assertThat(erroEsperado).isNotNull();
		
	}

}
