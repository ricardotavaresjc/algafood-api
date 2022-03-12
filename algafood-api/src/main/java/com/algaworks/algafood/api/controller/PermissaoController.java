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
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import com.algaworks.algafood.domain.service.PermissaoService;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

	@Autowired
	private PermissaoService service;

	@Autowired
	private PermissaoRepository repository;

	@GetMapping
	public List<Permissao> listar(){
		return repository.findAll();
	}

	@GetMapping("/{buscaId}")
	public ResponseEntity<Permissao> buscar(@PathVariable Long buscaId){
		Optional<Permissao> permissao = repository.findById(buscaId);

		if(permissao.isPresent()) {
			return ResponseEntity.ok(permissao.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Permissao adicionar(@RequestBody Permissao permissao){
		return service.salvar(permissao);
	}

	@PutMapping("/{buscarId}")
	public ResponseEntity<Permissao> atualizar(@PathVariable Long buscarId, @RequestBody Permissao permissao){
		Optional<Permissao> permissaoAtual = repository.findById(buscarId);

		if(permissaoAtual.isPresent()) {
			BeanUtils.copyProperties(permissao, permissaoAtual.get(),"id");
			Permissao permissaoSalva = service.salvar(permissaoAtual.get());

			return ResponseEntity.ok(permissaoSalva);
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{permissaoId}")
	public ResponseEntity<?> deletar(@PathVariable Long permissaoId){
		try {
			service.excluir(permissaoId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		}
	}

}
