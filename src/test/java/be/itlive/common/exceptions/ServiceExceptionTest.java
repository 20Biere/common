package be.itlive.common.exceptions;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for ServiceException.
 * @author vbiertho
 *
 */
public class ServiceExceptionTest {

    private static final String DEFAULT_MESSAGE = "java.lang.Throwable";

    @Test
    public void testServiceException() throws Exception {
        // Act
        ServiceException e = new ServiceException();

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testServiceExceptionString() throws Exception {
        /**
         * Correct message
         */
        // Arrange
        String message = "exception message";

        // Act
        ServiceException e = new ServiceException(message);

        // Assert
        assertEquals(message, e.getMessage());
        assertEquals(message, e.getLocalizedMessage());
        assertNull(e.getCause());

        /**
         * Empty message
         */
        // Arrange
        message = "";

        // Act
        e = new ServiceException(message);

        // Assert
        assertEquals(message, e.getMessage());
        assertEquals(message, e.getLocalizedMessage());
        assertNull(e.getCause());

        /**
         * Null message
         */
        // Arrange
        message = null;

        // Act
        e = new ServiceException(message);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testServiceExceptionThrowable() throws Exception {
        /**
         * Correct cause
         */
        // Arrange
        Throwable cause = new Throwable();

        // Act
        ServiceException e = new ServiceException(cause);

        // Assert
        assertEquals(DEFAULT_MESSAGE, e.getMessage());
        assertEquals(DEFAULT_MESSAGE, e.getLocalizedMessage());
        assertEquals(cause, e.getCause());

        /**
         * Null cause
         */
        // Arrange
        cause = null;

        // Act
        e = new ServiceException(cause);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testServiceExceptionStringThrowable() throws Exception {
        /**
         * Correct message, correct cause
         */
        // Arrange
        String message = "exception message";
        Throwable cause = new Throwable();

        // Act
        ServiceException e = new ServiceException(message, cause);

        // Assert
        assertEquals(message, e.getMessage());
        assertEquals(message, e.getLocalizedMessage());
        assertEquals(cause, e.getCause());

        /**
         * Empty message, correct cause
         */
        // Arrange
        message = "";

        // Act
        e = new ServiceException(message, cause);

        // Assert
        assertEquals(message, e.getMessage());
        assertEquals(message, e.getLocalizedMessage());
        assertEquals(cause, e.getCause());

        /**
         * Null message, correct cause
         */
        // Arrange
        message = null;

        // Act
        e = new ServiceException(message, cause);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertEquals(cause, e.getCause());

        /**
         * Correct message, null cause
         */
        // Arrange
        message = "exception message";
        cause = null;

        // Act
        e = new ServiceException(message, cause);

        // Assert
        assertEquals(message, e.getMessage());
        assertEquals(message, e.getLocalizedMessage());
        assertNull(e.getCause());

        /**
         * Empty message, null cause
         */
        // Arrange
        message = "";

        // Act
        e = new ServiceException(message, cause);

        // Assert
        assertEquals(message, e.getMessage());
        assertEquals(message, e.getLocalizedMessage());
        assertNull(e.getCause());

        /**
         * Null message, null cause
         */
        // Arrange
        message = null;

        // Act
        e = new ServiceException(message, cause);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

}
