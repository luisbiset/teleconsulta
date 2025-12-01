package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.PacienteEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class PacienteRepository {

    @Inject
    private EntityManager em;

    public void salvar(PacienteEntity paciente) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (paciente.getId() == null) {
                em.persist(paciente);   
            } else {
                em.merge(paciente);     
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
            PacienteEntity p = em.find(PacienteEntity.class, id);
            if (p != null) em.remove(p);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public PacienteEntity findById(Long id) {
        return em.find(PacienteEntity.class, id);
    }

    public List<PacienteEntity> listarTodos() {
        return em.createQuery(
            "SELECT p FROM PacienteEntity p ORDER BY p.id",
            PacienteEntity.class
        ).getResultList();
    }

    public PacienteEntity findByUsuario(Long idUsuario) {
        try {
            return em.createQuery(
                "SELECT p FROM PacienteEntity p WHERE p.usuario.id = :idUsuario",
                PacienteEntity.class
            )
            .setParameter("idUsuario", idUsuario)
            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

