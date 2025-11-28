package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.util.HibernateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class UsuarioRepository {

	public void salvar(UsuarioEntity usuario) {
		EntityManager em = HibernateUtil.getEntityManager();

		try {
			em.getTransaction().begin();

			if (usuario.getId() == null) {
				em.persist(usuario);
			} else {
				em.merge(usuario);
			}

			em.getTransaction().commit();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback(); // ✅ ESSENCIAL
			}
			throw e;

		} finally {
			em.close(); // ✅ ESSENCIAL
		}
	}

	public UsuarioEntity buscarPorId(Long id) {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.find(UsuarioEntity.class, id);
		} finally {
			em.close();
		}
	}

	public UsuarioEntity buscarPorLogin(String login) {

		EntityManager em = HibernateUtil.getEntityManager();

		return em.createQuery("SELECT u FROM UsuarioEntity u WHERE u.email = :login", UsuarioEntity.class)
				.setParameter("login", login).getResultStream().findFirst().orElse(null);
	}

	public List<UsuarioEntity> listarTodos() {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			return em.createQuery("SELECT u FROM UsuarioEntity u ORDER BY u.nome", UsuarioEntity.class).getResultList();
		} finally {
			em.close();
		}
	}

	public void excluir(Long id) {
		EntityManager em = HibernateUtil.getEntityManager();

		try {
			em.getTransaction().begin();

			UsuarioEntity usuario = em.find(UsuarioEntity.class, id);
			if (usuario != null) {
				em.remove(usuario);
			}

			em.getTransaction().commit();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;

		} finally {
			em.close();
		}
	}
	
	public UsuarioEntity buscarPorCpf(String cpf) {

	    EntityManager em = HibernateUtil.getEntityManager();

	    try {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
	        Root<UsuarioEntity> root = cq.from(UsuarioEntity.class);

	        // remove pontos e traço do CPF do banco
	        cq.select(root).where(
	            cb.equal(
	                cb.function(
	                    "replace", String.class,
	                    cb.function(
	                        "replace", String.class,
	                        root.get("cpf"),
	                        cb.literal("."),
	                        cb.literal("")
	                    ),
	                    cb.literal("-"),
	                    cb.literal("")
	                ),
	                cpf
	            )
	        );

	        return em.createQuery(cq)
	                 .getResultStream()
	                 .findFirst()
	                 .orElse(null);

	    } finally {
	        em.close();
	    }
	}

	
	



}
