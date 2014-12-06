package net.dev.jcd.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.dev.jcd.model.Application;

/**
 * <p>Project WARP</p>
 *
 * @author jcdwyer
 *
 */
@ApplicationScoped
public class ApplicationRepository {

    @Inject
    private EntityManager em;

    public Application findById(Long id) {
        return em.find(Application.class, id);
    }

    public Application findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Application> criteria = cb.createQuery(Application.class);
        Root<Application> app = criteria.from(Application.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(app).where(cb.equal(app.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Application> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Application> criteria = cb.createQuery(Application.class);
        Root<Application> app = criteria.from(Application.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(app).orderBy(cb.asc(app.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
