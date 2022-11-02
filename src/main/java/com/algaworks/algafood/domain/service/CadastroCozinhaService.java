package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de codigo %d não pode ser removida, pois está em uso";
	
	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}
	
	@Transactional
	public void excluir(Long cozinhaId) {
		//
		/*Trate aqui as exceções de negocio
		 * Esse try trata erros que possam vir oriundos do
		banco, como quando a cozinha não existe e violação de integridade quando tenta excluir alguma
		cozinha que tenha relacionamento
		*/
		try {
			cozinhaRepository.deleteById(cozinhaId);
			/*
			 * Utilizado para garantir que o JPA vai descarregar o que tem 
			 * em memoria para o banco e não ficar carregando em uma fila 
			 * Isso faz com que ele caia no tratamento abaixo que estamos fazendo
			 */
			cozinhaRepository.flush();
			
		} catch (EmptyResultDataAccessException e){
			throw new CozinhaNaoEncontradaException(cozinhaId);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}
	
	public Cozinha buscarOuFalhar(Long cozinhaId) {
		return cozinhaRepository.findById(cozinhaId)
				.orElseThrow(()->new CozinhaNaoEncontradaException(cozinhaId));
	}

}
