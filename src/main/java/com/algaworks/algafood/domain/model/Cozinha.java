package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.algaworks.algafood.core.validation.Groups;
import com.algaworks.algafood.core.validation.Groups.CozinhaId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {

	//@NotNull(groups = Groups.CozinhaId.class) - -validado pela dto
	@EqualsAndHashCode.Include	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String nome;
	
	//boa pratica iniciar o array vazio para evitar null pointer quando chamar cozinha
	//precisa mapear a propriedade que esta sendo relacionada do outro lado
	//Tomar cuidade nesse caso com o loop infinito na hora de montar o json, caso essa seja a classe serializada
	//por isso foi colocado o JsonIgnore
	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();

}
