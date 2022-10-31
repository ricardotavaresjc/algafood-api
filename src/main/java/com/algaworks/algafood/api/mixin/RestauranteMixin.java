package com.algaworks.algafood.api.mixin;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class RestauranteMixin {
	
	@JsonIgnoreProperties(value = "nome", allowGetters = true)
	private Cozinha cozinha;

	@JsonIgnore //Deve ser avaliado se precisa de carregar essa propriedade ou não na representação
	private Endereco endereco;

	//@JsonIgnore
	private OffsetDateTime dataCadastro;

	//@JsonIgnore
	private OffsetDateTime dataAtualizacao;


	@JsonIgnore //para não mostrar no payload e deixa-lo grande
	private List<FormaPagamento> formasPagamento = new ArrayList<>();

	@JsonIgnore
	private List<Produto> produtos = new ArrayList<>();

}
