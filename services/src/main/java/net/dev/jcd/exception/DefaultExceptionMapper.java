package net.dev.jcd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.arquillian.container.test.impl.client.deployment.ValidationException;

/**
 * Handle {@link ValidationException}
 *
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {
	@Inject
	private Logger log;

	/**
	 * Creates a JAX-RS "Internal Server Error" response including a map containing the error
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */

	@Override
	public Response toResponse(Exception exception) {
		log.fine("Exception");
		Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("validation", exception.getLocalizedMessage());

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
	}

}
