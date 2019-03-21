package be.itlive.common.exceptions;

import javax.ejb.ApplicationException;

/**
 * Class representing an exception into the business logic.
 * @author vbiertho
 *
 */
@ApplicationException(rollback = false)
public class BusinessException extends Exception {

    private static final long serialVersionUID = -1448799097721384193L;

    /**
     * Call the class Exception.
     */
    public BusinessException() {
        super();
    }

    /**
     * Call the class Exception with the given message.
     * @param message the exception message
     */
    public BusinessException(final String message) {
        super(message);
    }

    /**
     * Call the class Exception with the given initial cause.
     * @param cause the initial cause
     */
    public BusinessException(final Throwable cause) {
        super(cause);
    }

    /**
     * Call the class Exception with the given message and initial cause.
     * @param message the exception message
     * @param cause the initial cause
     */
    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
