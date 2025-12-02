package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

@ApplicationScoped
public class UnidadeSaudeRepository {

    @Inject
    private EntityManager em;

    public void salvar(UnidadeSaudeEntity unidade) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (unidade.getId() == null) {
                em.persist(unidade);
            } else {
                em.merge(unidade);
            }
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
            UnidadeSaudeEntity unidade = em.find(UnidadeSaudeEntity.class, id);
            if (unidade != null) em.remove(unidade);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public UnidadeSaudeEntity findById(Long id) {
        return em.find(UnidadeSaudeEntity.class, id);
    }

    public List<UnidadeSaudeEntity> listarTodos() {
        return em.createQuery(
            "SELECT u FROM UnidadeSaudeEntity u ORDER BY u.nome",
            UnidadeSaudeEntity.class
        ).getResultList();
    }

	
}
