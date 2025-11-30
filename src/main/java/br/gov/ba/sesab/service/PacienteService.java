package br.gov.ba.sesab.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.PacienteRepository;
import br.gov.ba.sesab.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PacienteService {

    @Inject
    private PacienteRepository pacienteRepository;
    
    @Inject UsuarioRepository usuarioRepository;

  
    
    @Transactional
    public void salvar(PacienteEntity paciente) {
        pacienteRepository.salvar(paciente);
    }

    @Transactional
    public void salvarComUsuario(PacienteEntity paciente) {

        UsuarioEntity usuarioTela = paciente.getUsuario();

        if (usuarioTela == null) {
            throw new IllegalArgumentException("Usuário é obrigatório.");
        }

        if (usuarioTela.getCpf() == null || usuarioTela.getCpf().isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }

        String cpfLimpo = usuarioTela.getCpf().replaceAll("\\D", "");
        usuarioTela.setCpf(cpfLimpo);

        UsuarioEntity usuarioExistente =
                usuarioRepository.buscarPorCpf(cpfLimpo);

        if (paciente.getId() == null) {

            if (usuarioExistente != null) {

            	if (usuarioExistente.getPaciente() != null) {
            	    throw new RuntimeException("Já existe um paciente com esse CPF.");
            	}

                usuarioExistente.setNome(usuarioTela.getNome());
                usuarioExistente.setEmail(usuarioTela.getEmail());
                usuarioExistente.setAtivo(usuarioTela.isAtivo());

                paciente.setUsuario(usuarioExistente);

            } else {

                usuarioTela.setSenha(UUID.randomUUID().toString());
                usuarioTela.setPerfil(PerfilUsuario.PACIENTE);
                usuarioTela.setAtivo(false);
                usuarioTela.setDataCadastro(new Date());

                // ✅ SALVA PRIMEIRO O USUÁRIO
                usuarioRepository.salvar(usuarioTela);

                paciente.setUsuario(usuarioTela);
            }

        } else {
            PacienteEntity pacienteBanco =
                    pacienteRepository.findById(paciente.getId());

            if (pacienteBanco == null) {
                throw new RuntimeException("Paciente não encontrado.");
            }

            paciente.setUsuario(pacienteBanco.getUsuario());
        }

        pacienteRepository.salvar(paciente);
    }
    
    public PacienteEntity findById(Long id) {
    	
    	return pacienteRepository.findById(id);
    }


    @Transactional
    public void excluir(Long idPaciente) {

        PacienteEntity paciente = pacienteRepository.findById(idPaciente);

        if (paciente == null) {
            throw new RuntimeException("Paciente não encontrado.");
        }

        pacienteRepository.excluir(idPaciente);
    }

    public List<PacienteEntity> listarTodos() {
        return pacienteRepository.listarTodos();
    }

	

	public PacienteEntity findByUsuario(Long id) {
		
		return pacienteRepository.findByUsuario(id);
	}
}
