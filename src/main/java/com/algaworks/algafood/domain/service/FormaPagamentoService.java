package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {

	private static final String MSG_FORMA_DE_PAGAMENTO_EM_USO = "Forma de Pagamento de codigo %d não pode ser removida, pois está em uso";
	@Autowired
	private FormaPagamentoRepository repository;

	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return repository.save(formaPagamento);
	}

	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
			/*
			 * Utilizado para garantir que o JPA vai descarregar o que tem 
			 * em memoria para o banco e não ficar carregando em uma fila 
			 * Isso faz com que ele caia no tratamento abaixo que estamos fazendo
			 */
			repository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new FormaPagamentoNaoEncontradoException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_FORMA_DE_PAGAMENTO_EM_USO, id));
		}
	}
	
	public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
		
		return repository.findById(formaPagamentoId).orElseThrow(()-> new FormaPagamentoNaoEncontradoException(formaPagamentoId));
	}


}
