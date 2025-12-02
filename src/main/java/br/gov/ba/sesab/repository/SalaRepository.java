package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.SalaEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@ApplicationScoped
public class SalaRepository {

    @Inject
    private EntityManager em;

    public void salvar(SalaEntity sala) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sala);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void excluir(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SalaEntity s = em.find(SalaEntity.class, id);
            if (s != null) em.remove(s);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public SalaEntity findById(Long id) {
        return em.find(SalaEntity.class, id);
    }

    public List<SalaEntity> listarTodas() {
        return em.createQuery(
            "SELECT s FROM SalaEntity s",
            SalaEntity.class
        ).getResultList();
    }

    public List<SalaEntity> buscarPorUnidade(Long idUnidade) {
        return em.createQuery(
            "SELECT s FROM SalaEntity s WHERE s.unidade.id = :id",
            SalaEntity.class
        )
        .setParameter("id", idUnidade)
        .getResultList();
    }
}

