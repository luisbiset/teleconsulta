package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class UnidadeSaudeRepository {

    public void salvar(UnidadeSaudeEntity unidade) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (unidade.getId() == null) {
                em.persist(unidade);
            } else {
                em.merge(unidade);
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

            UnidadeSaudeEntity unidade = em.find(UnidadeSaudeEntity.class, id);
            if (unidade != null) {
                em.remove(unidade);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public UnidadeSaudeEntity buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(UnidadeSaudeEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<UnidadeSaudeEntity> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM UnidadeSaudeEntity u ORDER BY u.nome",
                    UnidadeSaudeEntity.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}
