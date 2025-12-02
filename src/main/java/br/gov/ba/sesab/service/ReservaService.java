package br.gov.ba.sesab.service;

import java.time.LocalDateTime;
import java.util.List;

import br.gov.ba.sesab.entity.ReservaEntity;
import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.ReservaRepository;
import br.gov.ba.sesab.util.SessaoUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ReservaService {

    @Inject
    private ReservaRepository reservaRepository;

    public void salvar(ReservaEntity reserva) {

        boolean existeConflito = reservaRepository.existeConflito(
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

        reservaRepository.salvar(reserva);
    }

    @Transactional
    public void excluir(Long idReserva) {

        UsuarioEntity logado = SessaoUtil.getUsuarioLogado();

        if (logado == null) {
            throw new RuntimeException("Usuário não autenticado.");
        }

        ReservaEntity reserva = reservaRepository.findById(idReserva);

        if (reserva == null) {
            throw new RuntimeException("Reserva não encontrada.");
        }

        boolean reservaJaOcorreu =
                reserva.getDataInicio().isBefore(LocalDateTime.now());

        if (logado.getPerfil() == PerfilUsuario.ADMIN) {
            reservaRepository.excluir(idReserva);
            return;
        }

        if (logado.getPerfil() == PerfilUsuario.ATENDENTE) {

            if (reservaJaOcorreu) {
                throw new RuntimeException(
                    "A reserva já ocorreu e não pode mais ser cancelada."
                );
            }

            reservaRepository.excluir(idReserva);
            return;
        }

        throw new RuntimeException("Você não tem permissão para cancelar reservas.");
    }


    public List<ReservaEntity> listarTodas() {
        return reservaRepository.listarTodas();
    }

    public ReservaEntity buscarPorId(Long id) {
        return reservaRepository.findById(id);
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

        return reservaRepository.buscarSalasDisponiveis(idUnidade, inicio, fim);
    }
}

