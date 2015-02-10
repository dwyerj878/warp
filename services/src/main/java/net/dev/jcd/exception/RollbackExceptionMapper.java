package net.dev.jcd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.arquillian.container.test.impl.client.deployment.ValidationException;

/**
 * Handle {@link EJBTransactionRolledbackException}
 *
 */
@Provider
public class RollbackExceptionMapper implements ExceptionMapper<EJBTransactionRolledbackException> {
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
	public Response toResponse(EJBTransactionRolledbackException exception) {
		log.fine("EJBTransactionRolledbackException");
		Throwable ex = exception;
		while (true) {
			if (ex instanceof javax.validation.ValidationException)
				return new ValidationExceptionMapper().toResponse((javax.validation.ValidationException) ex);
			ex = ex.getCause();
			if (ex.getCause() == null || ex.getCause() == ex)
				break;
			
		}
		
		Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("validation", exception.getLocalizedMessage());

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
	}

}
