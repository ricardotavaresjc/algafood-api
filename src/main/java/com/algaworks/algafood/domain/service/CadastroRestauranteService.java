package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;

//	public Restaurante atualizar(Long id, Restaurante restaurante) {
//		Restaurante restauranteAtualizado = restauranteRepository.buscar(id);
//
//		if(restauranteAtualizado == null) {
//			throw new EntidadeNaoEncontradaException(
//					String.format("Não existe restaurante com o codigo %d", id));
//		}
//
//		BeanUtils.copyProperties(restaurante, restauranteAtualizado,"id");
//		return salvar(restauranteAtualizado);
//	}

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

		/*Metodo Substituido pelo cozinha.isEmpty do Optional
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com codigo %d",cozinhaID ));
		}
		*/
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);

		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void ativar(Long restauranteID) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteID);
		
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteID) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteID);
		
		restauranteAtual.inativar();		
	}
	
	//Caso haja erro, aconteça o rolback e nao fique dados inconsistente
	//Ou seja tudo que esse metodo executar não vai ser commitado em caso de erro
	@Transactional 
	public void ativar(List<Long> restaurantesIds) {
		restaurantesIds.forEach(this::ativar);
	}
	
	@Transactional 
	public void inativar(List<Long> restaurantesIds) {
		restaurantesIds.forEach(this::inativar);
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento =  formaPagamentoService.buscarOuFalhar(formaPagamentoId);
		
		restaurante.removerFormaPagamento(formaPagamento);		
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento =  formaPagamentoService.buscarOuFalhar(formaPagamentoId);
		
		restaurante.adicionarFormaPagamento(formaPagamento);		
	}
	
	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		
		restaurante.removerResponsavel(usuario);
	}
	
	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		
		restaurante.adicionarResponsavel(usuario);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		
		return restauranteRepository.findById(restauranteId)
				.orElseThrow(()-> new RestauranteNaoEncontradaException(restauranteId));
	}
	
	@Transactional
	public void abrir(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		
		restaurante.abrir();
	}
	
	@Transactional
	public void fechar(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		
		restaurante.fechar();
	}


}
