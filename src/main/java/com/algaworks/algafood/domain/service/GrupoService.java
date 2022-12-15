package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class GrupoService {
	
	private static final String MSG_GRUPO_EM_USO 
    = "Grupo de código %d não pode ser removido, pois está em uso";
	
	@Autowired
	public GrupoRepository repository;
	
	public Grupo salvar(Grupo grupo) {
		return repository.save(grupo);
	}
	
	public void excluir(Long grupoId) {

		try {
			repository.deleteById(grupoId);

			repository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(grupoId);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
		}

	}
	
	public Grupo buscarOuFalhar(Long grupoId) {
		return repository.findById(grupoId).orElseThrow(()->new GrupoNaoEncontradoException(grupoId));
	}
}
