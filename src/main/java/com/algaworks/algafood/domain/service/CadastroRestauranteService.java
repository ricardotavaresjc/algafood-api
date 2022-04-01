package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

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
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

		/*Metodo Substituido pelo cozinha.isEmpty do Optional
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com codigo %d",cozinhaID ));
		}
		*/
		
		restaurante.setCozinha(cozinha);

		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(()-> new RestauranteNaoEncontradaException(restauranteId));
	}


}
