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

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.controller.openapi.CidadeControllerOpenApi;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;




@RestController
@RequestMapping(path = "/cidades")
public class CidadeController implements CidadeControllerOpenApi {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeModelDisassembler;
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public List<CidadeModel> listar(){
		return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
	}

	
	@GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		return cidadeModelAssembler.toModel(cadastroCidadeService.buscarOuFalhar(cidadeId));

	}

	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@Override
	public CidadeModel adicionar(			
			@RequestBody @Valid CidadeInput cidadeInput) {

		try {
			Cidade cidade = cidadeModelDisassembler.toDomainObject(cidadeInput);
			return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidade));
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}
	
	
	@PutMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public CidadeModel atualizar(			
			@PathVariable Long cidadeId,			
			@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);
			cidadeModelDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidadeAtual));
			
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		

	}

	
	
	@DeleteMapping(value = "/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Override
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);

	}	

}
