package be.itlive.common.exceptions;

import javax.ejb.ApplicationException;

/**
 * Class representing an exception during a DB access.
 * @author vbiertho
 *
 */
@ApplicationException(rollback = true)
public class DaoException extends Exception {

    private static final long serialVersionUID = -2006086688216468967L;

    /**
     * Call the class Exception.
     */
    public DaoException() {
        super();
    }

    /**
     * Call the class Exception with the given message.
     * @param message the exception message
     */
    public DaoException(final String message) {
        super(message);
    }

    /**
     * Call the class Exception with the given initial cause.
     * @param cause the initial cause
     */
    public DaoException(final Throwable cause) {
        super(cause);
    }

    /**
     * Call the class Exception with the given message and initial cause.
     * @param message the exception message
     * @param cause the initial cause
     */
    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
