package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;

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

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

		/*Metodo Substituido pelo cozinha.isEmpty do Optional
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com codigo %d",cozinhaID ));
		}
		*/
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);

		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void ativar(Long restauranteID) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteID);
		
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteID) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteID);
		
		restauranteAtual.inativar();		
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(()-> new RestauranteNaoEncontradaException(restauranteId));
	}


}
