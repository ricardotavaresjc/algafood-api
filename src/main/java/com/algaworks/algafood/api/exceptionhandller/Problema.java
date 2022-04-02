package com.algaworks.algafood.api.exceptionhandller;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder//padrão de projeto para construir um objeto, nao precisando do new
@Getter
public class Problema {
	private LocalDateTime dataHora;
	private String mensagem;
}
