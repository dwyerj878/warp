package net.dev.jcd.service;

import net.dev.jcd.data.UserListProducer;
import net.dev.jcd.data.UserRepository;
import net.dev.jcd.model.User;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;
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
    private UserRepository userRepository;
    
    @Inject
    private UserListProducer userListProducer;

    @Inject
    private Event<User> userEventSrc;

    public void save(User user) throws Exception {
        log.info("Registering " + user.getName());
        em.persist(user);
        userEventSrc.fire(user);
    }

	/**
	 * @param id
	 * @return
	 */
	public User findById(long id) {
		return userRepository.findById(id);
	}

	/**
	 * @return
	 */
	public List<User> getUsers() {
		return userListProducer.getUsers();
	}

	/**
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
