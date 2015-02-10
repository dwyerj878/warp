package net.dev.jcd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Handle {@link ValidationException}
 *
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
	@Inject
	private Logger log;

	/**
	 * Creates a JAX-RS "Conflict" response including a map containing the error
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */

	@Override
	public Response toResponse(ValidationException exception) {
		log.fine("Validation exception");
		Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("validation", exception.getLocalizedMessage());

		return Response.status(Response.Status.CONFLICT).entity(responseObj).build();
	}

}
