package br.gov.ba.sesab.service;

import java.util.Date;
import java.util.List;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.PacienteRepository;
import br.gov.ba.sesab.repository.UsuarioRepository;
import br.gov.ba.sesab.util.SessaoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioService {

	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Inject
	PacienteRepository pacienteRepository;
	
	
	@Transactional
	public void salvar(UsuarioEntity usuario) {

	    UsuarioEntity logado = SessaoUtil.getUsuarioLogado();

	    if (logado == null || logado.getPerfil() != PerfilUsuario.ADMIN) {
	        throw new IllegalArgumentException("Apenas ADMIN pode salvar usuários.");
	    }

	    if (usuario.getDataCadastro() == null) {
	        usuario.setDataCadastro(new Date());
	    }

	    if (usuario.getId() == null) {
	        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
	            throw new IllegalArgumentException("Senha é obrigatória para novo usuário.");
	        }

	        if (usuario.getPerfil() == null) {
	            usuario.setPerfil(PerfilUsuario.ATENDENTE);
	        }
	    } 
	    else {
	        UsuarioEntity usuarioBanco = usuarioRepository.buscarPorId(usuario.getId());

	        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
	            usuario.setSenha(usuarioBanco.getSenha());
	        }

	        if (usuario.getPerfil() == null) {
	            usuario.setPerfil(usuarioBanco.getPerfil());
	        }
	    }

	    UsuarioEntity existentePorEmail =
	            usuarioRepository.buscarPorLogin(usuario.getEmail());

	    if (existentePorEmail != null &&
	        (usuario.getId() == null || !existentePorEmail.getId().equals(usuario.getId()))) {

	        throw new IllegalArgumentException("Já existe um usuário cadastrado com este email.");
	    }

	    usuarioRepository.salvar(usuario);
	}

	public List<UsuarioEntity> listarSolicitantes() {
	    return usuarioRepository.listarUsuariosSolicitantes();
	}


	public UsuarioEntity buscarPorId(Long id) {
		return usuarioRepository.buscarPorId(id);
	}

	public UsuarioEntity buscarPorLogin(String login) {
		return usuarioRepository.buscarPorLogin(login);
	}

	public List<UsuarioEntity> listarTodos() {
		return usuarioRepository.listarTodos();
	}

	@Transactional
	public void excluir(Long id) {

	    UsuarioEntity logado = SessaoUtil.getUsuarioLogado();

	    if (logado == null || logado.getPerfil() != PerfilUsuario.ADMIN) {
	        throw new RuntimeException("Apenas ADMIN pode excluir usuários.");
	    }

	    UsuarioEntity usuario = usuarioRepository.buscarPorId(id);

	    if (usuario == null) {
	        throw new RuntimeException("Usuário não encontrado.");
	    }

	    if (usuario.getPaciente() != null) {
	        pacienteRepository.excluir(usuario.getPaciente().getId());
	    }

	    usuarioRepository.excluir(id);
	}


	 public UsuarioEntity buscarUsuarioPorCpf(String cpf) {
	        return usuarioRepository.buscarPorCpf(cpf);
	    }
	

	public UsuarioEntity autenticar(String cpf, String senha) {
		UsuarioEntity usuario = usuarioRepository.buscarPorCpf(cpf);

		if (usuario != null && usuario.getSenha().trim().equals(senha.trim())) {
			return usuario;
		}

		return null;
	}

	

}
