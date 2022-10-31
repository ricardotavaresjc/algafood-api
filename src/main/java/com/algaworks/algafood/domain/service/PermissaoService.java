package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@Service
public class PermissaoService {

	private static final String MSG_PERMISSAO_EM_USO = "Permissao de código %d não pode ser removida, pois está em uso";
	@Autowired
	private PermissaoRepository repository;

	@Transactional
	public Permissao salvar(Permissao permissao) {
		return repository.save(permissao);
	}

	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new PermissaoNaoEncontradaException(id);

		}catch (DataIntegrityViolationException e) {
                throw new EntidadeEmUsoException(
                    String.format(MSG_PERMISSAO_EM_USO, id));
        }
	}
}
