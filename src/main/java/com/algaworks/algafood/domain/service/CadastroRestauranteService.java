package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CozinhaRepository cozinhaRepository;

//	public Restaurante atualizar(Long id, Restaurante restaurante) {
//		Restaurante restauranteAtualizado = restauranteRepository.buscar(id);
//
//		if(restauranteAtualizado == null) {
//			throw new EntidadeNaoEncontradaException(
//					String.format("Não existe restaurante com o codigo %d", id));
//		}
//
//		BeanUtils.copyProperties(restaurante, restauranteAtualizado,"id");
//		return salvar(restauranteAtualizado);
//	}

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaID = restaurante.getCozinha().getId();
		Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaID);

		/*Metodo Substituido pelo cozinha.isEmpty do Optional
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com codigo %d",cozinhaID ));
		}
		*/

		if (cozinha.isEmpty()) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com codigo %d",cozinhaID ));
		}
		restaurante.setCozinha(cozinha.get());

		return restauranteRepository.save(restaurante);
	}


}
