package com.algaworks.algafood;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroDeCozinhaApiTest {
	
	@LocalServerPort
	private int port;
	
	@Test
	public void deveRetornarStatus200_quandoConsultarCozinhas() {
		RestAssured.given()
			.basePath("/cozinhas")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
//	//Validando o corpo
	@Test
	public void deveRetorna4Cozinhas_quandoCadastrarCozinha() {
		RestAssured.given()
			.basePath("/cozinhas")
			.port(port)
			.accept(ContentType.JSON)				
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(4))
			.body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
		
		//Matchers biblioteca para fazer correspondencias
	}
	
//	@Test
//	public void deveRetornarStatus201_quandoCadastrarCozinha() {
//		RestAssured.given()
//			.basePath("/cozinhas")
//			.port(port)
//			.accept(ContentType.JSON)
//			.contentType(ContentType.JSON)		
//			.when()
//			.post()
//		.then()
//			.statusCode(HttpStatus.CREATED.value());
//	}
}
