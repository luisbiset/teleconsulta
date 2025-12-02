package br.gov.ba.sesab.service;

import java.util.List;

import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.repository.ReservaRepository;
import br.gov.ba.sesab.repository.SalaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SalaService extends AbstractService{

    @Inject
    private SalaRepository salaRepository;
    
    @Inject
    private ReservaRepository reservaRepository;

    @Transactional
    public void salvar(SalaEntity sala) {
    	salaRepository.salvar(sala);
    }

    

    @Transactional
    public void excluir(Long id) {

        validarAdmin(); 

        SalaEntity sala = salaRepository.findById(id);

        if (sala == null) {
            throw new RuntimeException("Sala não encontrada.");
        }

        if (reservaRepository.existeReservaPorSala(id)) {
            throw new RuntimeException(
                "Não é possível excluir a sala pois existem reservas vinculadas."
            );
        }

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
