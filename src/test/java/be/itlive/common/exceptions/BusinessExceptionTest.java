package be.itlive.common.exceptions;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for BusinessException.
 * @author vbiertho
 *
 */
public class BusinessExceptionTest {

    private static final String DEFAULT_MESSAGE = "java.lang.Throwable";

    @Test
    public void testBusinessException() throws Exception {
        // Act
        BusinessException e = new BusinessException();

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testBusinessExceptionString() throws Exception {
        /**
         * Correct message
         */
        // Arrange
        String message = "exception message";

        // Act
        BusinessException e = new BusinessException(message);

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
        e = new BusinessException(message);

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
        e = new BusinessException(message);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testBusinessExceptionThrowable() throws Exception {
        /**
         * Correct cause
         */
        // Arrange
        Throwable cause = new Throwable();

        // Act
        BusinessException e = new BusinessException(cause);

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
        e = new BusinessException(cause);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testBusinessExceptionStringThrowable() throws Exception {
        /**
         * Correct message, correct cause
         */
        // Arrange
        String message = "exception message";
        Throwable cause = new Throwable();

        // Act
        BusinessException e = new BusinessException(message, cause);

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
        e = new BusinessException(message, cause);

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
        e = new BusinessException(message, cause);

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
        e = new BusinessException(message, cause);

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
        e = new BusinessException(message, cause);

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
        e = new BusinessException(message, cause);

        // Assert
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

}
