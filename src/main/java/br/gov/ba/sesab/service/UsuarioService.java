package br.gov.ba.sesab.service;

import java.util.Date;
import java.util.List;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.UsuarioRepository;
import br.gov.ba.sesab.util.SessaoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioService {

	@Inject
	private UsuarioRepository usuarioRepository;
	
	public void salvar(UsuarioEntity usuario) {

	    UsuarioEntity logado = SessaoUtil.getUsuarioLogado();

	    if (logado == null || logado.getPerfil() != PerfilUsuario.ADMIN) {
	        throw new RuntimeException("Apenas ADMIN pode salvar usuários.");
	    }

	    if (usuario.getDataCadastro() == null) {
	        usuario.setDataCadastro(new Date());
	    }

	    usuarioRepository.salvar(usuario);
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

	public void excluir(Long id) {

	    UsuarioEntity logado = SessaoUtil.getUsuarioLogado();

	    if (logado == null || logado.getPerfil() != PerfilUsuario.ADMIN) {
	        throw new RuntimeException("Apenas ADMIN pode excluir usuários.");
	    }

	    usuarioRepository.excluir(id);
	}


	public UsuarioEntity autenticar(String cpf, String senha) {
		UsuarioEntity usuario = usuarioRepository.buscarPorCpf(cpf);

		if (usuario != null && usuario.getSenha().trim().equals(senha.trim())) {
			return usuario;
		}

		return null;
	}

	@Transactional
	public void criarUsuarioInicial() {

		UsuarioEntity existente = usuarioRepository.buscarPorCpf("12345678900");

		if (existente != null) {
			System.out.println("ℹ️ Usuário inicial já existe.");
			return;
		}

		UsuarioEntity u = new UsuarioEntity();
		u.setNome("Usuário Inicial");
		u.setEmail("admin@teleconsulta.com");
		u.setCpf("12345678900");
		u.setSenha("123"); // enquanto não usa criptografia
		u.setDataCadastro(new Date());

		usuarioRepository.salvar(u);

		System.out.println("✅ Usuário inicial criado com sucesso!");
	}

}
