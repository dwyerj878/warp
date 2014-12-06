package net.dev.jcd.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import net.dev.jcd.model.Application;

/**
 * <p>Project WARP</p>
 *
 * @author jcdwyer
 *
 */
@RequestScoped
public class ApplicationListProducer {

    @Inject
    private ApplicationRepository memberRepository;

    private List<Application> members;

    // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    @Produces
    @Named
    public List<Application> getApplications() {
        return members;
    }

    public void onApplicationListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Application member) {
        retrieveAllApplicationsOrderedByName();
    }

    @PostConstruct
    public void retrieveAllApplicationsOrderedByName() {
        members = memberRepository.findAllOrderedByName();
    }
}
