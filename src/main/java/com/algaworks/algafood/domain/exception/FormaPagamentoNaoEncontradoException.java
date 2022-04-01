package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;


	public FormaPagamentoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public FormaPagamentoNaoEncontradoException(Long formaPagamentoId) {
		this(String.format("Não foi encontrado forma de pagamento com o id %d", formaPagamentoId));
	}
	

}
