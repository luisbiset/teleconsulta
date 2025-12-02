package br.gov.ba.sesab.repository;

import java.time.LocalDateTime;
import java.util.List;

import br.gov.ba.sesab.entity.ReservaEntity;
import br.gov.ba.sesab.entity.SalaEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@ApplicationScoped
public class ReservaRepository {

    @Inject
    private EntityManager em;

    public void salvar(ReservaEntity reserva) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (reserva.getId() == null) {
                em.persist(reserva);
            } else {
                em.merge(reserva);
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void excluir(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            ReservaEntity r = em.find(ReservaEntity.class, id);
            if (r != null) {
                em.remove(r);
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public ReservaEntity findById(Long id) {
        return em.find(ReservaEntity.class, id);
    }

    public List<ReservaEntity> listarTodas() {
        return em.createQuery(
                "SELECT r FROM ReservaEntity r ORDER BY r.dataInicio",
                ReservaEntity.class
        ).getResultList();
    }

    public boolean existeConflito(
            Long idReserva,
            Long salaId,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {

        String jpql = """
            SELECT COUNT(r)
            FROM ReservaEntity r
            WHERE r.sala.id = :salaId
            AND (
                (:inicio BETWEEN r.dataInicio AND r.dataFim)
                OR
                (:fim BETWEEN r.dataInicio AND r.dataFim)
                OR
                (r.dataInicio BETWEEN :inicio AND :fim)
            )
        """;

        if (idReserva != null) {
            jpql += " AND r.id <> :idReserva";
        }

        var query = em.createQuery(jpql, Long.class)
                .setParameter("salaId", salaId)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim);

        if (idReserva != null) {
            query.setParameter("idReserva", idReserva);
        }

        Long total = query.getSingleResult();
        return total > 0;
    }

    public List<SalaEntity> buscarSalasDisponiveis(
            Long idUnidade,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return em.createQuery("""
            SELECT s
            FROM SalaEntity s
            WHERE s.unidade.id = :idUnidade
            AND s.id NOT IN (
                SELECT r.sala.id
                FROM ReservaEntity r
                WHERE (
                    (:inicio BETWEEN r.dataInicio AND r.dataFim)
                    OR
                    (:fim BETWEEN r.dataInicio AND r.dataFim)
                    OR
                    (r.dataInicio BETWEEN :inicio AND :fim)
                )
            )
        """, SalaEntity.class)
        .setParameter("idUnidade", idUnidade)
        .setParameter("inicio", inicio)
        .setParameter("fim", fim)
        .getResultList();
    }
    
    public boolean existeReservaPorPaciente(Long idPaciente) {
        Long total = em.createQuery(
            "SELECT COUNT(r) " +
            "FROM ReservaEntity r " +
            "WHERE r.usuarioSolicitante.id = (" +
            "   SELECT p.usuario.id FROM PacienteEntity p WHERE p.id = :idPaciente" +
            ")",
            Long.class
        )
        .setParameter("idPaciente", idPaciente)
        .getSingleResult();

        return total > 0;
    }


    public boolean existeReservaPorSala(Long idSala) {
        Long total = em.createQuery("""
            SELECT COUNT(r)
            FROM ReservaEntity r
            WHERE r.sala.id = :idSala
        """, Long.class)
        .setParameter("idSala", idSala)
        .getSingleResult();

        return total > 0;
    }

    public boolean existeReservaPorUnidade(Long idUnidade) {
        Long total = em.createQuery("""
            SELECT COUNT(r)
            FROM ReservaEntity r
            WHERE r.sala.unidade.id = :idUnidade
        """, Long.class)
        .setParameter("idUnidade", idUnidade)
        .getSingleResult();

        return total > 0;
    }

    public boolean existeReservaPorUsuario(Long idUsuario) {

        Long total = em.createQuery(
            "SELECT COUNT(r) " +
            "FROM ReservaEntity r " +
            "WHERE r.usuarioSolicitante.id = :idUsuario",
            Long.class
        )
        .setParameter("idUsuario", idUsuario)
        .getSingleResult();

        return total > 0;
    }


}

