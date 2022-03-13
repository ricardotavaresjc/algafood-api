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
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository repository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@GetMapping
	public List<Cozinha> listar(){
		return repository.findAll();
	}


	@GetMapping("/{buscarId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable(name = "buscarId") Long id) {
		Optional<Cozinha> cozinha = repository.findById(id);

		if(cozinha.isPresent()) {
			return ResponseEntity.ok(cozinha.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Cozinha adcionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinha.salvar(cozinha);
	}

	@PutMapping("/{buscarId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long buscarId, @RequestBody Cozinha cozinha){
		Optional<Cozinha> cozinhaAtual = repository.findById(buscarId);

		//Caso exista ele atualiza, se não ele retorna um status 400 not found
		if (cozinhaAtual.isPresent()){
			/*usado para copiar as propriedades de um objeto para o outro, nesse caso ele so esta ignorando "id"
			para nao copiar um id nullo. Com se fosse cozinhaAtual.setNome(cozinha.getNome)*/
			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(),"id");
			Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaAtual.get());

			return ResponseEntity.ok(cozinhaSalva);

		}

		return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId){

		try {
			cadastroCozinha.excluir(cozinhaId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		}

	}

}
