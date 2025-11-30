package br.gov.ba.sesab.service;

import java.util.List;

import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.repository.SalaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SalaService {

    @Inject
    private SalaRepository repository;

    @Transactional
    public void salvar(SalaEntity sala) {
        repository.salvar(sala);
    }

    @Transactional
    public void excluir(Long id) {
        repository.excluir(id);
    }
    
    public List<SalaEntity> listarTodas() {
        return repository.listarTodas();
    }

    public SalaEntity findById(Long id) {
        return repository.findById(id);
    }
}
