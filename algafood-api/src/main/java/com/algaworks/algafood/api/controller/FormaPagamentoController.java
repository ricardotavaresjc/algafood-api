package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.FormaPagamentoService;

@RestController
@RequestMapping("/formasPagamento")
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository repository;

	@Autowired
	private FormaPagamentoService service;

	@GetMapping
	public List<FormaPagamento> listar(){
		return repository.findAll();
	}

	@GetMapping("/{formaPagamentoId}")
	public ResponseEntity<FormaPagamento> buscar(@PathVariable(name = "formaPagamentoId") Long id) {
		Optional<FormaPagamento> formaPagamento = repository.findById(id);

		if(formaPagamento.isPresent()) {
			return ResponseEntity.ok(formaPagamento.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamento adicionar(@RequestBody FormaPagamento formaPagamento) {
		return service.salvar(formaPagamento);
	}

	@PutMapping("/{formaPagamentoID}")
	public ResponseEntity<FormaPagamento> atualizar(@PathVariable(name = "formaPagamentoID") Long id, @RequestBody FormaPagamento formaPagamento){
		Optional<FormaPagamento> formaPagamentoAtual = repository.findById(id);

		if(formaPagamentoAtual.isPresent()) {
			BeanUtils.copyProperties(formaPagamento, formaPagamentoAtual.get(),"id");

			FormaPagamento formaPagamentoSalvo = service.salvar(formaPagamentoAtual.get());

			return ResponseEntity.ok(formaPagamentoSalvo);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{formaPagamentoID}")
	public ResponseEntity<?> remover(@PathVariable(name = "formaPagamentoID") Long id){
		try {
			service.excluir(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
		catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}



}
