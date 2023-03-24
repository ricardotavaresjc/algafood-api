package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Cidades")
@Tag(name = "Cidades", description = "Gerencia as Cidades")
@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeModelDisassembler;

	@ApiOperation(value = "Lista as cidades")
	@GetMapping
	public List<CidadeModel> listar(){
		return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
	}

	@ApiOperation(value = "Busca uma cidade por ID", httpMethod = "GET")
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@ApiParam(value = "Id de uma cidade", example = "1") @PathVariable Long cidadeId) {
		return cidadeModelAssembler.toModel(cadastroCidadeService.buscarOuFalhar(cidadeId));

	}

	@ApiOperation(value = "Cadastra uma cidade", httpMethod = "POST")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(
			@ApiParam(name="body", value ="Representação de uma cidade")
			@RequestBody @Valid CidadeInput cidadeInput) {

		try {
			Cidade cidade = cidadeModelDisassembler.toDomainObject(cidadeInput);
			return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidade));
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@ApiOperation(value = "Atualiza uma cidade por ID", httpMethod = "PUT")
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(
			@ApiParam(value = "Id de uma cidade", example = "1") 
			@PathVariable Long cidadeId,
			@ApiParam(name="body", value ="Representação de uma nova cidade")
			@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);
			cidadeModelDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidadeAtual));
			
		} catch (EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		

	}

	@ApiOperation(value = "Exclui uma cidade por ID", httpMethod = "@DELETE")
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "Id de uma cidade", example = "1") @PathVariable Long cidadeId) {
		cadastroCidadeService.excluir(cidadeId);

	}	

}
