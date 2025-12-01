package br.gov.ba.sesab.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class EntityManagerProducer {

    @Produces
    @RequestScoped
    public EntityManager createEntityManager() {
        return HibernateStartupListener.getEmf().createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}
