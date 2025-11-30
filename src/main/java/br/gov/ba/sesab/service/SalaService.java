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
    private SalaRepository salaRepository;

    @Transactional
    public void salvar(SalaEntity sala) {
    	salaRepository.salvar(sala);
    }

    @Transactional
    public void excluir(Long id) {
    	salaRepository.excluir(id);
    }
    
    public List<SalaEntity> listarTodas() {
        return salaRepository.listarTodas();
    }

    public SalaEntity findById(Long id) {
        return salaRepository.findById(id);
    }
    
    public List<SalaEntity> listarPorUnidade(Long idUnidade) {
        return salaRepository.buscarPorUnidade(idUnidade);
    }

}
