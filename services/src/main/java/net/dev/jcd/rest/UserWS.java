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

import net.dev.jcd.model.User;
import net.dev.jcd.service.UserService;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the users
 * table.
 */
@Path("/users")
@RequestScoped
public class UserWS {

	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private UserService userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> listAllUsers() {
		return userService.getUsers();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public User lookupUserById(@PathParam("id") long id) {
		User user = userService.findById(id);
		if (user == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return user;
	}

	/**
	 * Creates a new user from the values provided. Performs validation, and
	 * will return a JAX-RS response with either 200 ok, or with a map of
	 * fields, and related errors.
	 * 
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User member) throws Exception {

		log.info("adding member :" + member);
		// Validates member using bean validation
		validateUser(member);

		User result = userService.save(member);

		// Create an "ok" response
		return Response.ok(result).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id:[0-9][0-9]*}")
	public Response updateUser(@PathParam("id") long id, User member) throws Exception {

		log.info("updating member :" + member);
		// Validates member using bean validation
		validateUser(member);

		User result = userService.save(member);

		// Create an "ok" response
		return Response.ok(result).build();

	}

	/**
	 * <p>
	 * Validates the given Member variable and throws validation exceptions
	 * based on the type of error. If the error is standard bean validation
	 * errors then it will throw a ConstraintValidationException with the set of
	 * the constraints violated.
	 * </p>
	 * <p>
	 * If the error is caused because an existing member with the same email is
	 * registered it throws a regular validation exception so that it can be
	 * interpreted separately.
	 * </p>
	 * 
	 * @param user
	 *            Member to be validated
	 * @throws ConstraintViolationException
	 *             If Bean Validation errors exist
	 * @throws ValidationException
	 *             If member with the same email already exists
	 */
	private void validateUser(User user) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<User>> violations = validator.validate(user);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		// Check the uniqueness of the email address
		if (emailAlreadyExists(user)) {
			throw new ValidationException("Unique Email Violation");
		}
	}

	/**
	 * Checks if a member with the same email address is already registered.
	 * This is the only way to easily capture the
	 * "@UniqueConstraint(columnNames = "email")" constraint from the Member
	 * class.
	 * 
	 * @param user
	 *            The email to check
	 * @return True if the email already exists, and false otherwise
	 */
	public boolean emailAlreadyExists(User user) {
		User foundUser = null;
		try {
			foundUser = userService.findByEmail(user.getEmail());
		} catch (NoResultException e) {
			// ignore
		} catch (Exception e) {
			if (e.getCause() instanceof NoResultException)
				log.fine("Not found");
			else
				throw e;
		}
		return foundUser != null && foundUser.getId() != user.getId();
	}
}
