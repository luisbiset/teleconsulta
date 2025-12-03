package br.gov.ba.sesab.config;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HibernateStartupListener implements ServletContextListener {

    private static EntityManagerFactory emf;
    final Logger log = Logger.getLogger(HibernateStartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	
    	  
    	 
    	log.info("=============================================");
    	log.info(" INICIALIZANDO HIBERNATE (Startup)");
    	log.info(" Ambiente: LOCAL");
    	log.info(" Usando JDBC do persistence.xml");
    	log.info("=============================================");

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

            log.info(" Ambiente: DOCKER");
            log.info(" Banco: " + dbUrl);

        } else {
        	
        	log.info("=============================================");
        	log.info(" Ambiente: LOCAL");
            log.info(" Usando JDBC definido no persistence.xml");
            log.info("=============================================");
        }

        emf = Persistence.createEntityManagerFactory("teleconsultaPU", props);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
            log.info("=============================================");
            log.info(" Hibernate finalizado.");
            log.info("=============================================");
        }
    }

    public static EntityManagerFactory getEmf() {
        return emf;
    }
}
