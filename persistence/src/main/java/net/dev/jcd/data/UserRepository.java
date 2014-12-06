package net.dev.jcd.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import net.dev.jcd.model.User;

/**
 * <p>Project WARP</p>
 *
 * @author jcdwyer
 *
 */
@ApplicationScoped
public class UserRepository {

    @Inject
    private EntityManager em;

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(user).where(cb.equal(user.get("email"), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<User> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(user).orderBy(cb.asc(user.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
