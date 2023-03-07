package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.model.view.RestauranteView;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	
	@Autowired
	private SmartValidator validator;
	
	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteModelDisassembler;

	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}
	
	@JsonView(RestauranteView.ApenasNome.class)
	@GetMapping(params = "projecao=apenas-nome")
	public List<RestauranteModel> listarApenasNomes() {
		return listar();
	}
	
//Outra forma de fazer mais dinamica
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteModel> restaurantesModel = restauranteModelAssembler.toCollectionModel(restaurantes);
//		
//		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesModel);
//		
//		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
//		
//		if ("apenas-nome".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//		} else if ("completo".equals(projecao)) {
//			restaurantesWrapper.setSerializationView(null);
//		}
//		
//		return restaurantesWrapper;
//	}
	
//	@GetMapping
//	public List<RestauranteModel> listar() {
//		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
//	}
//	
//	@JsonView(RestauranteView.Resumo.class)
//	@GetMapping(params = "projecao=resumo")
//	public List<RestauranteModel> listarResumido() {
//		return listar();
//	}
//
//	@JsonView(RestauranteView.ApenasNome.class)
//	@GetMapping(params = "projecao=apenas-nome")
//	public List<RestauranteModel> listarApenasNomes() {
//		return listar();
//	}

	@GetMapping("/{restauranteID}")
	public RestauranteModel buscar(@PathVariable Long restauranteID) {
		Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteID);
		
		return restauranteModelAssembler.toModel(restaurante);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		Restaurante restaurante = restauranteModelDisassembler.toDomainObject(restauranteInput);
		try {
			return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException |  CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput) {

		try {
			Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
			restauranteModelDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
			return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException |  CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}
	
	@PutMapping("/{restauranteId}/ativo") 
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.inativar(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
		cadastroRestauranteService.abrir(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		cadastroRestauranteService.fechar(restauranteId);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestauranteService.ativar(restaurantesIds);
		} catch (RestauranteNaoEncontradaException e) {
			//Usado para forçar devolver o codigo 400 ao inves de 404
			//Quando restaurante passado inexistente
			throw new NegocioException(e.getMessage(),e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestauranteService.inativar(restaurantesIds);
		} catch (RestauranteNaoEncontradaException e) {
			//Usado para forçar devolver o codigo 400 ao inves de 404
			//Quando restaurante passado inexistente
			throw new NegocioException(e.getMessage(),e);
		}
		
	}
//	@PatchMapping("/{restauranteId}")
//	public Restaurante atualizarParcial(@PathVariable Long restauranteId, 
//			@RequestBody Map<String, Object> campos, HttpServletRequest request) {
//		Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);
//
//		merge(campos, restauranteAtual, request);
//		validate(restauranteAtual, "restaurante");
//
//		return atualizar(restauranteId, restauranteAtual);
//	}
//	
//	private void validate(Restaurante restaurante, String objectName) {
//		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
//		validator.validate(restaurante, bindingResult);
//		
//		if (bindingResult.hasErrors()) {
//			throw new ValidacaoException(bindingResult);
//		}
//	}
//
//	/**
//	 * Cada propriedade dos dadosOrigem que veio no mapa deve ser passada para
//	 * restauranteDestino usando a API de Reflections do Spring
//	 */
//	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
//		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
//		try {
//			
//			/*
//			 * ObjectMapper responsavel por converter(serializar) json em Obj Java ou vice versa
//			 * objectMapper.convertValue(dadosOrigem, Restaurante.class) esta convertendo o que esta vindo
//			 * no corpo para restaurante
//			 */
//			ObjectMapper objectMapper = new ObjectMapper();
//			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
//			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
//			
//			dadosOrigem.forEach((nomePropriedade, valorPropriedade)-> {
//				/*Declaração do Field do java.lang
//				 * field representa o atributo de restaurante que queremos modificar
//				 * ReflectionUtils é do Spring
//				 * o findField busca na classe Restaurante a propriedade que esta vindo em tempo de execução
//				 * REtorna a instancia de um campo
//				 */
//				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
//				
//				//Usado para permitir acesso aos metodos privados da classe Restaurante. Torna a variavel privada acessivel
//				field.setAccessible(true);
//				
//				/*
//				 * retorna o valor de um campo, o valor da propriedade
//				 */
//				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
//				
//				/*pega o valor na recuperado na instancia Field field e modifica para o novo valor
//				 *
//				 */
//				ReflectionUtils.setField(field, restauranteDestino, novoValor);
//		
//		});
//		}catch(IllegalArgumentException e){
//			Throwable rootCause = ExceptionUtils.getRootCause(e);
//			throw new HttpMessageNotReadableException(e.getMessage(),rootCause, serverHttpRequest);
//		}
//
//	}
	
	
}
