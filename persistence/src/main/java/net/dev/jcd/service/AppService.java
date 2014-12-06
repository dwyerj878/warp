package net.dev.jcd.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.dev.jcd.model.Application;


/**
 * <p>Project WARP</p>
 * 
 * <p>Persistence Services for {@link Application}</p>
 *
 * @author jcdwyer
 *
 */
//The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class AppService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Application> memberEventSrc;

    public void save(Application application) throws Exception {
        log.info("Registering " + application.getName());
        if (application.getId() == null)
        	em.persist(application);
        else 
        {
        	Application existing = em.find(Application.class, application.getId());
        	existing.setName(application.getName());
        	existing.setProperties(application.getProperties());
        	existing.setEnvironments(application.getEnvironments());
        	em.merge(existing);
        }
        memberEventSrc.fire(application);
    }
}
