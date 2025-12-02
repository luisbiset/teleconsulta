package br.gov.ba.sesab.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class HibernateStartupListener implements ServletContextListener {

    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Inicializando Hibernate...");

        Map<String, Object> props = new HashMap<>();

        String env = System.getenv("APP_ENV");

        if ("docker".equalsIgnoreCase(env)) {

            String sqlitePath = System.getenv("SQLITE_PATH");
            String dbName     = System.getenv("SQLITE_FILENAME");

            String dbUrl = "jdbc:sqlite:" + sqlitePath + "/" + dbName;

            props.put("jakarta.persistence.jdbc.url", dbUrl);
            props.put("hibernate.hbm2ddl.auto", "create");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.format_sql", "true");

            System.out.println("📌 Ambiente: DOCKER");
            System.out.println("📌 Banco: " + dbUrl);

        } else {

            System.out.println("📌 Ambiente: LOCAL");
            System.out.println("📌 Usando JDBC definido no persistence.xml");
        }

        emf = Persistence.createEntityManagerFactory("teleconsultaPU", props);
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
