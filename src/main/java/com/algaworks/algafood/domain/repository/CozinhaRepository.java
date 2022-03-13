package com.algaworks.algafood.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{

	//Busca pela propriedade nome da entidade
	public List<Cozinha> findByNome(String nome);

	//Busca pela propriedade nome mas com um criterio like
	public List<Cozinha> findByNomeContaining(String nome);


}
