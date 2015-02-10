/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dev.jcd.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.dev.jcd.model.Application;
import net.dev.jcd.service.AppService;

/**
 *
 * Services to manage {@link Application} resources
 * 
 */
@Path("/apps")
@RequestScoped
public class AppWS {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;


    @Inject
    private AppService appService;
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Application> listAllApplications() {
        return appService.findAllOrderedByName();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Application lookupMemberById(@PathParam("id") long id) {
        Application application = appService.findById(id);
        if (application == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return application;
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     * @throws Exception 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createApp(Application app) throws Exception {

		log.info("Saving Application:" + app);
		// Validates member using bean validation
		validateMember(app);

		Application result = appService.save(app);

		// Create an "ok" response
		return Response.ok(result).build();
	}
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id:[0-9][0-9]*}")
	public Response updateApp(@PathParam("id") long id, Application app) throws Exception {
		log.info("updating app :" + app);
		// Validates member using bean validation
		validateMember(app);

		Application result = appService.save(app);

		// Create an "ok" response
		return Response.ok(result).build();
	}

    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param app Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
    private void validateMember(Application app) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Application>> violations = validator.validate(app);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (app.getId() == null && nameAlreadyExists(app.getName())) {
            throw new ValidationException("Unique Name Violation");
        }
    }
    

    /**
     * Checks if an application with the same name is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "name")" constraint from the {@link Application} class.
     * 
     * @param name  to check
     * @return True if the name already exists, and false otherwise
     */
    public boolean nameAlreadyExists(String name) {
        Application app = null;
        try {
            app = appService.findByName(name);
        } catch (NoResultException e) {
        	log.warning(e.getLocalizedMessage());
        } catch (EJBException e) {
        	log.warning(e.getLocalizedMessage());
        }
        return app != null;
    }
}
