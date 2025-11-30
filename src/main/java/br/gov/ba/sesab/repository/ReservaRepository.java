package br.gov.ba.sesab.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import br.gov.ba.sesab.entity.ReservaEntity;
import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ReservaRepository {

    public void salvar(ReservaEntity reserva) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (reserva.getId() == null) {
                em.persist(reserva);
            } else {
                em.merge(reserva);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ReservaEntity r = em.find(ReservaEntity.class, id);
            if (r != null) {
                em.remove(r);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public ReservaEntity buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(ReservaEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<ReservaEntity> listarTodas() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT r FROM ReservaEntity r ORDER BY r.dataInicio",
                ReservaEntity.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existeConflito(
            Long idReserva,
            Long salaId,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
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

        } finally {
            em.close();
        }
    }

    public List<SalaEntity> buscarSalasDisponiveis(
            Long idUnidade,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
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
        } finally {
            em.close();
        }
    }
}
