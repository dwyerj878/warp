package net.dev.jcd.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.dev.jcd.data.ApplicationListProducer;
import net.dev.jcd.data.ApplicationRepository;
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
    private Event<Application> appEventSrc;
    
    @Inject
    private ApplicationListProducer appListProducer;
    
    @Inject 
    private ApplicationRepository appRepository;

    public Application save(Application application) throws Exception {
        log.info("Saving " + application.getName());
        if (application.getId() == null)
        	em.persist(application);
        else 
        {
        	Application saved = em.merge(application);
//        	em.persist(application);
//        	Application existing = em.find(Application.class, application.getId());
//        	existing.setName(application.getName());
//        	existing.getProperties().clear();
//        	existing.getProperties().addAll(application.getProperties());
//        	existing.setEnvironments(application.getEnvironments());
//        	em.persist(existing);
        }
        appEventSrc.fire(application);
        return application;
    }

	/**
	 * @param id
	 * @return
	 */
	public Application findById(long id) {
		return appRepository.findById(id);
	}

	/**
	 * @return
	 */
	public List<Application> findAllOrderedByName() {
		return appListProducer.getApplications();
	}

	/**
	 * @param name
	 * @return
	 */
	public Application findByName(String name) {
		return appRepository.findByName(name);
	}
	
	
}
