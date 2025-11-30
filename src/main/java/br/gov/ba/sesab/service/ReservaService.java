package br.gov.ba.sesab.service;

import java.time.LocalDateTime;
import java.util.List;

import br.gov.ba.sesab.entity.ReservaEntity;
import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.repository.ReservaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReservaService {

    @Inject
    private ReservaRepository repository;

    public void salvar(ReservaEntity reserva) {

        boolean existeConflito = repository.existeConflito(
            reserva.getId(),
            reserva.getSala().getId(),
            reserva.getDataInicio(),
            reserva.getDataFim()
        );

        if (existeConflito) {
            throw new RuntimeException(
                "Já existe uma reserva neste dia e horário para esta sala."
            );
        }

        repository.salvar(reserva);
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }

    public List<ReservaEntity> listarTodas() {
        return repository.listarTodas();
    }

    public ReservaEntity buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }
    public List<SalaEntity> consultarDisponibilidade(
            Long idUnidade,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {

        if (idUnidade == null || inicio == null || fim == null) {
            throw new RuntimeException("Todos os filtros são obrigatórios.");
        }

        if (fim.isBefore(inicio)) {
            throw new RuntimeException("A data final não pode ser menor que a inicial.");
        }

        return repository.buscarSalasDisponiveis(idUnidade, inicio, fim);
    }
}

