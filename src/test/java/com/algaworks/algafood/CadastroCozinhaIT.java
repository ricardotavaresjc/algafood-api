package com.algaworks.algafood;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {
	private static final int COZINHA_ID_INEXISTENTE = 100;
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private CozinhaRepository repository;
	
	private Cozinha cozinhaAmericana;
	private int quantidadeCozinha;
	private String jsonCorretoCozinhaChinesa;
	
	
	//Metodo para ser executado antes de cada teste - callback
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";		
		
		jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
				"/json/correto/cozinha-chinesa.json");
		
		//responsavel por limpar o banco de dados de teste
		databaseCleaner.clearTables();
		//Responsavel por preencher
		prepararDados();
	}
	
	@Test
	public void deveRetornarStatus200_quandoConsultarCozinhas() {
		RestAssured.given()			
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
//	//Validando o corpo
	@Test
	public void deveRetornaQuantidadeCorretaDeCozinhas_quandoCadastrarCozinha() {
		RestAssured.given()			
			.accept(ContentType.JSON)				
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(quantidadeCozinha));			
		
		//Matchers biblioteca para fazer correspondencias
	}
	
	@Test
	public void testeDeveRetornarStatus201_quandoCadastrarCozinha() {
		RestAssured.given()
			.body(jsonCorretoCozinhaChinesa)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)		
			.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	//Teste de endpoint passando parametro url
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
			RestAssured.given()		
				.pathParam("cozinhaId", cozinhaAmericana.getId())
				.accept(ContentType.JSON)
			.when()
				.get("/{cozinhaId}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("nome", equalTo(cozinhaAmericana.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {
		RestAssured.given()		
			.pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
			
	}
	
	//metodo responsavel por popular o banco de dados
	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		repository.save(cozinhaTailandesa);
		
		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		repository.save(cozinhaAmericana);
		
		quantidadeCozinha = (int) repository.count();
		
	}
	
	
}
