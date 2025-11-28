package br.gov.ba.sesab.util;

import br.gov.ba.sesab.entity.UsuarioEntity;

public class TesteHibernate {

    public static void main(String[] args) {

        System.out.println("Iniciando teste do Hibernate...");

        var em = HibernateUtil.getEntityManager();

        System.out.println("EntityManager criado com sucesso: " + em);
        
        UsuarioEntity u = new UsuarioEntity();


        em.close();
        HibernateUtil.close();

        System.out.println("Teste finalizado com sucesso!");
    }
}
