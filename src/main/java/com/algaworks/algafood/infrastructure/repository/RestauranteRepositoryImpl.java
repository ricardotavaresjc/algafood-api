package com.algaworks.algafood.infrastructure.repository;

import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired @Lazy
	private RestauranteRepository restauranteRepository;

	//Implementação consulta dinamica
	@Override
	public List<Restaurante> find(String nome,
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){

		//construtor das clausulas
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		
		

		//é o mesmo que "from Restaurante"
		//Root é a raiz
		Root<Restaurante> root =  criteria.from(Restaurante.class);

		//Criar um list de predicates
		var predicates = new ArrayList<Predicate>();

		//Criar os predicados(filtros) de forma dinamica para passar no where
		if(StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}

		if(taxaFreteInicial != null) {
			predicates.add(builder
					.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}

		if(taxaFreteFinal != null) {
			predicates.add(builder
				.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}

		//a clausula Where
		//varios predicates ele fara o end entre eles
		//where espera um array, foi necessario converter list para array
		criteria.where(predicates.toArray(new Predicate[0]));

		//PRecisar criar uma TypedQuery para retornar esse tipo
		TypedQuery<Restaurante> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	
	//Implementação rigida
//	@Override
//	public List<Restaurante> find(String nome,
//			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
//
//		var jqpl = "from Restaurante where nome like :nome "
//				+ "and taxaFrete between :taxaInicial and :taxaFinal";
//
//		//não permite colocar % direto na string com a instrução jqpl
//		return manager.createQuery(jqpl, Restaurante.class)
//				.setParameter("nome", "%" + nome + "%")
//				.setParameter("taxaInicial", taxaFreteInicial)
//				.setParameter("taxaFinal", taxaFreteFinal)
//				.getResultList();
//	}
	
	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		return restauranteRepository.findAll(comFreteGratis()
				.and(comNomeSemelhante(nome)));
	}

}
