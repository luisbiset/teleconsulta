package br.gov.ba.sesab.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HibernateStartupListener implements ServletContextListener {

    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Inicializando Hibernate no startup...");
        emf = Persistence.createEntityManagerFactory("teleconsultaPU");
        System.out.println("✅ Hibernate inicializado com sucesso!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("🛑 Hibernate finalizado.");
        }
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }
}
