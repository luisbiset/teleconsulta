package br.gov.ba.sesab.repository;

import java.util.List;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class UsuarioRepository {

    @Inject
    private EntityManager em;

    public void salvar(UsuarioEntity usuario) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                em.merge(usuario);
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public UsuarioEntity findById(Long id) {
        return em.find(UsuarioEntity.class, id);
    }

    public UsuarioEntity buscarPorLogin(String login) {
        return em.createQuery(
                "SELECT u FROM UsuarioEntity u WHERE u.email = :login",
                UsuarioEntity.class)
                .setParameter("login", login)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<UsuarioEntity> listarTodos() {
        return em.createQuery(
                "SELECT u FROM UsuarioEntity u ORDER BY u.nome",
                UsuarioEntity.class)
                .getResultList();
    }

    public void excluir(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            UsuarioEntity usuario = em.find(UsuarioEntity.class, id);
            if (usuario != null) {
                em.remove(usuario);
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public UsuarioEntity buscarPorCpf(String cpf) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntity> cq = cb.createQuery(UsuarioEntity.class);
        Root<UsuarioEntity> root = cq.from(UsuarioEntity.class);

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
    }

    public List<UsuarioEntity> listarUsuariosSolicitantes() {
        return em.createQuery("""
            SELECT u FROM UsuarioEntity u
            WHERE u.perfil NOT IN (:perfis)
            ORDER BY u.nome
        """, UsuarioEntity.class)
        .setParameter("perfis",
                List.of(PerfilUsuario.ADMIN, PerfilUsuario.ATENDENTE))
        .getResultList();
    }
}
