package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.SalaEntity;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class SalaRepository {

    private EntityManager em;

    @PostConstruct
    public void init() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("teleconsultaPU");
        em = emf.createEntityManager();
    }

    public void salvar(SalaEntity sala) {
        em.getTransaction().begin();
        em.merge(sala);
        em.getTransaction().commit();
    }

    public List<SalaEntity> listarTodas() {
        return em.createQuery(
                "SELECT s FROM SalaEntity s", SalaEntity.class
        ).getResultList();
    }

    public void excluir(Long id) {
        SalaEntity s = em.find(SalaEntity.class, id);
        if (s != null) {
            em.getTransaction().begin();
            em.remove(s);
            em.getTransaction().commit();
        }
    }

    public SalaEntity findById(Long id) {
        return em.find(SalaEntity.class, id);
    }
    
    public List<SalaEntity> buscarPorUnidade(Long idUnidade) {
        return em.createQuery(
            "SELECT s FROM SalaEntity s WHERE s.unidade.id = :id", SalaEntity.class)
            .setParameter("id", idUnidade)
            .getResultList();
    }

}

