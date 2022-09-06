package com.algaworks.algafood.domain.exception;

public class NegocioException extends RuntimeException{

	private static final long serialVersionUID = 1L;


	public NegocioException(String mensagem) {
		super(mensagem);
	}
	
	//Pegando toda pilha da exceção
	public NegocioException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
