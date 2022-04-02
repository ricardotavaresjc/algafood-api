package com.algaworks.algafood.domain.exception;

public class NegociationException extends RuntimeException{

	private static final long serialVersionUID = 1L;


	public NegociationException(String mensagem) {
		super(mensagem);
	}
	
	//Pegando toda pilha da exceção
	public NegociationException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

}
