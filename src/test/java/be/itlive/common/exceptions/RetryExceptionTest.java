package be.itlive.common.exceptions;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class RetryExceptionTest {

    @Test
    public void testNewRetryException() throws Exception {
        // Given
        int count = 10;
        Exception cause = new RuntimeException();

        // When
        RetryException retryException = new RetryException(count, cause);

        // Then
        assertEquals(10, retryException.getMaxRetry());
        assertSame(cause, retryException.getCause());
    }

    @Test
    public void testRetryNoError() throws Exception {
        // Given
        Object retValue = new Object();
        Callable<Object> callable = mock(Callable.class);
        doReturn(retValue).when(callable).call();

        // When
        Object resValue = RetryException.retry(callable);

        // Then
        verify(callable).call();
        assertSame(retValue, resValue);
    }

    @Test
    public void testRetryRetryExceptionOnce() throws Exception {
        // Given
        final Exception cause = mock(Exception.class);
        final Object retValue = new Object();
        Callable<Object> callable = mock(Callable.class);
        doAnswer(new Answer<Object>() {

            boolean firstTime = true;

            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                if (firstTime) {
                    firstTime = false;
                    throw new RetryException(2, cause);
                } else {
                    return retValue;
                }
            }
        }).when(callable).call();

        // When
        Object value = RetryException.retry(callable);

        // Then
        assertSame(retValue, value);
        verify(callable, times(2)).call();
    }

    @Test
    public void testRetryRetryExceptionEachTime() throws Exception {
        // Given
        final Exception cause = mock(Exception.class);
        Callable<Object> callable = mock(Callable.class);
        doThrow(new RetryException(2, cause)).when(callable).call();

        // When
        try {
            RetryException.retry(callable);
            // Then
            fail("Sould throw cause");
        } catch (Exception e) {
            assertSame(cause, e);
            verify(callable, times(3)).call();
        }
    }

    @Test
    public void testRetryRetryExceptionEachTimeWithErrorCause() throws Exception {
        // Given
        final Error cause = mock(Error.class);
        Callable<Object> callable = mock(Callable.class);
        doThrow(new RetryException(2, cause)).when(callable).call();

        // When
        try {
            RetryException.retry(callable);
            // Then
            fail("Sould throw cause");
        } catch (Error e) {
            assertSame(cause, e);
            verify(callable, times(3)).call();
        }
    }

    @Test
    public void testRetryRetryExceptionEachTimeWithThrowableCause() throws Exception {
        // Given
        final Throwable cause = mock(Throwable.class);
        Callable<Object> callable = mock(Callable.class);
        doThrow(new RetryException(2, cause)).when(callable).call();

        // When
        try {
            RetryException.retry(callable);
            // Then
            fail("Sould throw cause");
        } catch (RuntimeException e) {
            assertSame(cause, e.getCause());
            verify(callable, times(3)).call();
        }
    }

    @Test
    public void testRetryOtherException() throws Exception {
        // Given
        Callable<Object> callable = mock(Callable.class);
        doThrow(RuntimeException.class).when(callable).call();

        // When
        try {
            RetryException.retry(callable);
            // Then
            fail("should throw Exception");
        } catch (RuntimeException e) {
            verify(callable, atMost(1)).call();
        }

    }

}
