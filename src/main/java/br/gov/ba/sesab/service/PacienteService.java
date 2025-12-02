package br.gov.ba.sesab.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.PacienteRepository;
import br.gov.ba.sesab.repository.ReservaRepository;
import br.gov.ba.sesab.repository.UsuarioRepository;
import br.gov.ba.sesab.util.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PacienteService extends AbstractService {

	@Inject
	private PacienteRepository pacienteRepository;

	@Inject
	UsuarioRepository usuarioRepository;

	@Inject
	private ReservaRepository reservaRepository;

	@Transactional
	public void salvarComUsuario(PacienteEntity paciente) {

	    UsuarioEntity usuarioTela = paciente.getUsuario();

	    String cpfLimpo = usuarioTela.getCpf().replaceAll("\\D", "");
	    usuarioTela.setCpf(cpfLimpo);

	    if (paciente.getId() == null) {

	        UsuarioEntity usuarioPorCpf =
	                usuarioRepository.buscarPorCpf(cpfLimpo);

	        if (usuarioPorCpf != null) {
	            throw new RuntimeException("Já existe um usuário cadastrado com este CPF.");
	        }

	        UsuarioEntity usuarioPorEmail =
	                usuarioRepository.buscarPorLogin(usuarioTela.getEmail());

	        if (usuarioPorEmail != null) {
	            throw new RuntimeException("Já existe um usuário cadastrado com este email.");
	        }
	        
	        if (paciente.getCns() == null || paciente.getCns().isBlank()) {
	            throw new RuntimeException("O CNS do paciente é obrigatório.");
	        }

	        PacienteEntity pacientePorCns =
	                pacienteRepository.buscarPorCns(paciente.getCns());

	        if (pacientePorCns != null &&
	            (paciente.getId() == null ||
	             !pacientePorCns.getId().equals(paciente.getId()))) {

	            throw new RuntimeException("Já existe um paciente cadastrado com este CNS.");
	        }

	        usuarioTela.setSenha(PasswordUtil.hash(UUID.randomUUID().toString()));
	        usuarioTela.setPerfil(PerfilUsuario.PACIENTE);
	        usuarioTela.setAtivo(false);
	        usuarioTela.setDataCadastro(new Date());

	        usuarioRepository.salvar(usuarioTela);
	        paciente.setUsuario(usuarioTela);
	    }
	    else {

	        PacienteEntity pacienteBanco =
	                pacienteRepository.findById(paciente.getId());

	        if (pacienteBanco == null) {
	            throw new RuntimeException("Paciente não encontrado.");
	        }

	        UsuarioEntity usuarioBanco = pacienteBanco.getUsuario();

	        usuarioBanco.setNome(usuarioTela.getNome());
	        usuarioBanco.setEmail(usuarioTela.getEmail());
	        usuarioBanco.setAtivo(usuarioTela.isAtivo());
	        usuarioBanco.setPerfil(PerfilUsuario.PACIENTE);


	        paciente.setUsuario(usuarioBanco);

	        usuarioRepository.salvar(usuarioBanco);
	    }

	    pacienteRepository.salvar(paciente);
	}


	public PacienteEntity findById(Long id) {

		return pacienteRepository.findById(id);
	}

	@Transactional
	public void excluir(Long idPaciente) {

		validarAdmin();

		PacienteEntity paciente = pacienteRepository.findById(idPaciente);

		if (paciente == null) {
			throw new RuntimeException("Paciente não encontrado.");
		}

		if (reservaRepository.existeReservaPorPaciente(idPaciente)) {
			throw new RuntimeException("Não é possível excluir o paciente pois existem reservas vinculadas.");
		}

		UsuarioEntity usuario = paciente.getUsuario();

		pacienteRepository.excluir(idPaciente);

		if (usuario != null) {
			usuarioRepository.excluir(usuario.getId());
		}
	}

	public List<PacienteEntity> listarTodos() {
		return pacienteRepository.listarTodos();
	}

	

	public PacienteEntity findByUsuario(Long id) {

		return pacienteRepository.findByUsuario(id);
	}
}
