package com.algaworks.algafood.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class Endereco {

	@Column(name = "endereco_cep")
	public String cep;

	@Column(name = "endereco_logradouro")
	public String logradouro;

	@Column(name = "endereco_numero")
	public String numero;

	@Column(name = "endereco_complemento")
	public String complemento;

	@Column(name = "endereco_bairro")
	public String bairro;

	@ManyToOne
	@JoinColumn(name = "endereco_cidade_id")
	public Cidade cidade;

}
