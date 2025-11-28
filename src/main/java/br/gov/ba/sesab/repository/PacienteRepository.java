package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class PacienteRepository {

    public void salvar(PacienteEntity paciente) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(paciente);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void atualizar(PacienteEntity paciente) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(paciente);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void excluir(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            PacienteEntity p = em.find(PacienteEntity.class, id);
            if (p != null) {
                em.remove(p);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public PacienteEntity buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(PacienteEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<PacienteEntity> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM PacienteEntity p ORDER BY p.nome",
                PacienteEntity.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}


