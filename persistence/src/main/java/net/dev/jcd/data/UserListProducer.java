package net.dev.jcd.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import net.dev.jcd.model.User;

/**
 * <p>Project WARP</p>
 *
 * @author jcdwyer
 *
 */
@RequestScoped
public class UserListProducer {

    @Inject
    private UserRepository userRepository;

    private List<User> users;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    @Produces
    @Named
    public List<User> getUsers() {
        return users;
    }

    public void onUserListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final User member) {
        retrieveAllUsersOrderedByName();
    }

    @PostConstruct
    public void retrieveAllUsersOrderedByName() {
        users = userRepository.findAllOrderedByName();
    }
}
