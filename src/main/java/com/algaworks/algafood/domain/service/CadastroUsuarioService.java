package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {
	
	@Autowired
	private UsuarioRepository repository; 
	
	
	@Transactional
	public Usuario salvar(Usuario usuario){
		
		Optional<Usuario> usuarioExistente = repository.findByEmail(usuario.getEmail());
		
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(
					String.format("Ja existe um usuário cadastrado com o email %s", usuario.getEmail()));
		}
		
		return repository.save(usuario);
	}
	
	@Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        
        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        
        usuario.setSenha(novaSenha);
    }

	
	public Usuario buscarOuFalhar(Long usuarioId){
		return repository.findById(usuarioId).orElseThrow(()->new UsuarioNaoEncontradoException(usuarioId));
	}

}
