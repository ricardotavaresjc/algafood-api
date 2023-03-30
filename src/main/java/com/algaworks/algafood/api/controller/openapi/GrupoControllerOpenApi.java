package com.algaworks.algafood.api.controller.openapi;

import java.util.List;

import com.algaworks.algafood.api.exceptionhandller.Problem;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Grupos")
@Tag(name = "Grupos",description = "Gerencia os grupos de usuários")
public interface GrupoControllerOpenApi {

	@ApiOperation(value = "Lista os grupos")
	List<GrupoModel> listar();

	@ApiOperation(value = "Busca um grupo por ID")
	@ApiResponses({
        @ApiResponse(code = 400, message = "ID da grupo inválido", response = Problem.class),
        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
	GrupoModel buscar(
			@ApiParam(value = "ID de um grupo", example = "1") 
			Long grupoId);

	@ApiOperation(value = "Cadastra um grupo")
	@ApiResponses({
        @ApiResponse(code = 201, message = "Grupo cadastrado"),
    })
	GrupoModel adcionar(
			@ApiParam(name = "body", value = "Representa um grupo")
			GrupoInput grupoInput);

	@ApiOperation(value = "Atualiza um grupo por ID")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Grupo atualizado"),
        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
	GrupoModel atualizar(
			@ApiParam(value = "ID de um grupo", example = "1") 
			Long grupoId, GrupoInput grupoInput);

	@ApiOperation(value = "Exclui um grupo por ID")
	@ApiResponses({
        @ApiResponse(code = 204, message = "Grupo excluído"),
        @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
	void remover(
			@ApiParam(value = "ID de um grupo", example = "1")
			Long grupoId);

}