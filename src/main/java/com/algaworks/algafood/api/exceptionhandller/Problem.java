package com.algaworks.algafood.api.exceptionhandller;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Builder//padrão de projeto para construir um objeto, nao precisando do new
@Getter
public class Problem {
	private Integer status;
	private String type;
	private String title;
	private String detail;	
	private String userMessage;
	private LocalDateTime timestamp;
	private List<Object> objects;
	
	@Getter
	@Builder
	public static class Object{
		private String name;
		private String userMessage;
	}
}
