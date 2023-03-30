package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.controller.openapi.GrupoControllerOpenApi;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;


@RestController
@RequestMapping(path = "/grupos")
public class GrupoController implements GrupoControllerOpenApi {
	
	@Autowired
	private CadastroGrupoService service;
	
	@Autowired
	private GrupoRepository repository;
	
	@Autowired
	private GrupoModelAssembler grupoAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler;
	
	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GrupoModel> listar(){
		var grupos = repository.findAll();
		return grupoAssembler.toCollectionModel(grupos);
	}
	
	@Override
	@GetMapping(value = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GrupoModel buscar(@PathVariable Long grupoId) {
		var grupo = service.buscarOuFalhar(grupoId);
		return grupoAssembler.toModel(grupo);
	}
	
	@Override
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adcionar(@RequestBody @Valid GrupoInput grupoInput) {
		var grupoNovo = grupoInputDisassembler.toDomainObject(grupoInput);
		return grupoAssembler.toModel(service.salvar(grupoNovo));
	}
	
	@Override
	@PutMapping(value = "/{grupoId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GrupoModel atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
		Grupo grupoAtual = service.buscarOuFalhar(grupoId);
		
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		
		grupoAtual = service.salvar(grupoAtual);
		
		return grupoAssembler.toModel(grupoAtual);
	}
	
	@Override
	@DeleteMapping(value = "/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long grupoId) {
		service.excluir(grupoId);
	}

}
