package net.dev.jcd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handle {@link ConstraintViolationException}
 *
 */
@Provider
public class ConstraintExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	@Inject
	private Logger log;

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation
	 * fields, and their message. This can then be used by clients to show
	 * violations.
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */

	@Override
	public Response toResponse(ConstraintViolationException exception) {

		Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
		log.fine("Validation completed. violations found: " + violations.size());
		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj).build();
	}

}
