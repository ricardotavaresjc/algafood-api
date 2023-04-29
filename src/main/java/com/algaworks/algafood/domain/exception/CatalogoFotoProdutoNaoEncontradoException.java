package com.algaworks.algafood.domain.exception;

public class CatalogoFotoProdutoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;


	public CatalogoFotoProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public CatalogoFotoProdutoNaoEncontradoException(Long restauranteId, Long produtoId) {
		this(String.format("Não existe um cadastro de foto com o código %d para o "
				+ "restaurante %d", produtoId, restauranteId));
	}
	

}
