package net.dev.jcd.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * <p>Handle {@link EJBTransactionRolledbackException}</p>
 * 
 * <p> {@link EJBException}s must be unrolled to find the root exception and then
 * we delegate to the appropriate injected {@link ExceptionMapper}</p>
 *
 */
@Provider
public class RollbackExceptionMapper implements ExceptionMapper<EJBTransactionRolledbackException> {
	@Inject
	private Logger log;

	@Inject
	private ConstraintExceptionMapper consMapper;
	
	@Inject
	private ValidationExceptionMapper validMapper;
	

	/**
	 * Creates a JAX-RS "Internal Server Error" response including a map
	 * containing the error
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	@Override
	public Response toResponse(EJBTransactionRolledbackException exception) {
		log.fine("EJBTransactionRolledbackException");
		Throwable ex = exception;
		if (findCause(javax.validation.ConstraintViolationException.class, exception) != null)			
			return consMapper.toResponse(findCause(javax.validation.ConstraintViolationException.class, exception));
		
		if (findCause(javax.validation.ValidationException.class, exception) != null)
			return validMapper.toResponse(findCause(javax.validation.ValidationException.class, exception));
		
		if (findCause(javax.persistence.PersistenceException.class, exception) != null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(((javax.persistence.PersistenceException) ex).getMessage()).build();
		

		Map<String, String> responseObj = new HashMap<String, String>();
		responseObj.put("error", exception.getLocalizedMessage());

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseObj).build();
	}
	
	@SuppressWarnings("unchecked")
	<T> T findCause(Class<T> clz, EJBTransactionRolledbackException e) {
		Throwable ex = e;
		while (true) {

			if (ex.getClass().equals(clz))
				return (T) ex;

			if (ex.getCause() == null || ex.getCause() == ex)
				break;

			ex = ex.getCause();

		}		
		
		return null;
		
	}

}
