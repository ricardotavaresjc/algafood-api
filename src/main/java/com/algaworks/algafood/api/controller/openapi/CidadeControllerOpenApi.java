package com.algaworks.algafood.api.controller.openapi;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandller.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Cidades")
@Tag(name = "Cidades", description = "Gerencia as Cidades")
public interface CidadeControllerOpenApi {
	@ApiOperation(value = "Lista as cidades")	
	public List<CidadeModel> listar();

	@ApiOperation(value = "Busca uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 400, message = "ID da cidade invalida",  response = Problem.class),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})	
	public CidadeModel buscar(
			@ApiParam(value = "Id de uma cidade", example = "1") 
			Long cidadeId);

	@ApiOperation(value = "Cadastra uma cidade")	
	public CidadeModel adicionar(
			@ApiParam(name="body", value ="Representação de uma cidade")
			CidadeInput cidadeInput);

	@ApiOperation(value = "Atualiza uma cidade por ID")	
	public CidadeModel atualizar(
			@ApiParam(value = "Id de uma cidade", example = "1") 
			Long cidadeId,
			@ApiParam(name="body", value ="Representação de uma nova cidade")
			CidadeInput cidadeInput);

	@ApiOperation(value = "Exclui uma cidade por ID")	
	public void remover(
			@ApiParam(value = "Id de uma cidade", example = "1") 
			Long cidadeId);
}
