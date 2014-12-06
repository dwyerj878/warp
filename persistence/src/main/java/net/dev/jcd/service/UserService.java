package net.dev.jcd.service;

import net.dev.jcd.model.User;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

/**
 * <p>Project WARP</p>
 *
 * <p>Persistence services for {@link User}</p>
 *
 * @author jcdwyer
 *
 */
@Stateless
public class UserService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<User> userEventSrc;

    public void save(User user) throws Exception {
        log.info("Registering " + user.getName());
        em.persist(user);
        userEventSrc.fire(user);
    }
}
