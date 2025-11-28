package br.gov.ba.sesab.service;

import java.util.List;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.repository.PacienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PacienteService {

    @Inject
    private PacienteRepository repository;

    public void salvar(PacienteEntity paciente) {
        if (paciente.getId() == null) {
            repository.salvar(paciente);
        } else {
            repository.atualizar(paciente);
        }
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }

    public PacienteEntity buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<PacienteEntity> listarTodos() {
        return repository.listarTodos();
    }
}


