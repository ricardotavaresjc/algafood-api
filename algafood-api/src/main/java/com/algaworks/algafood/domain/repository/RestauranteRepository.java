package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {

	@Override
	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();


	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

	//@Query(value = "from Restaurante wherenome like %:nome% and cozinha.id = :id")
//	List<Restaurante> consultarPorNome(String nome, @Param("id")Long cozinha);

	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);

}
