package com.algaworks.algafood.api.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {

	@NotBlank
	private String cep;	
	private String logradouro;
	private String numero;	
	private String complemento;	
	private String bairro;	
	private CidadeResumoModel cidade;
}
