package be.itlive.common.exceptions;

import java.util.concurrent.Callable;

import javax.ejb.ApplicationException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 *
 * Example of use :
 *
 * <pre>
 *   {@literal @}{@link Interceptors}({@link RetryInterceptor}.class)
 *   {@literal @}{@link TransactionAttribute}({@link TransactionAttributeType#NOT_SUPPORTED})
   public Object repeatableBusinessMethod(Object argument) throws SomeException {
 *       try {
 *           // Do some work that can throw SomeException an must be retried 3 times if so.
 *       } catch (SomeException e) {
 *           throw new {@link RetryException}(3, e);
 *       }
 *   }
 *
 * </pre>
 *
 * Example of use outside of EJB3:
 *
 * <pre>
 *   Object result = {@link RetryException#retry}(new {@link Callable}{@literal <Object>}() {
 *       {@literal @}{@link Override}
 *       public Object call() throws Exception {
 *           try {
 *               // Do some work that can throw SomeException an must be retried 3 times if so.
 *           } catch (SomeException e) {
 *               throw new {@link RetryException}(3, e);
 *           }
 *       }
 *   });
 * </pre>
 *
 *
 * @author vbiertho
 *
 */
@ApplicationException(rollback = false)
public class RetryException extends RuntimeException {

    private static final long serialVersionUID = -1238796289597165679L;

    private final int maxRetry;

    public int getMaxRetry() {
        return maxRetry;
    }

    /**
     * @param maxRetry Maximum number of execution
     * @param t cause of the exception
     */
    public RetryException(final int maxRetry, final Throwable t) {
        super(t);
        this.maxRetry = maxRetry;
    }

    /**
     * @param <T> return type of the call
     * @param callable Code to execute and retry on RetryException.
     * @return result of the call
     * @throws Exception exception throws by the call
     */
    public static <T> T retry(final Callable<T> callable) throws Exception {
        int count = 0;
        while (true) {
            count++;
            try {
                return callable.call();
            } catch (final RetryException e) {
                // treat RetryException only if max retry is reached
                if (e.getMaxRetry() < count) {
                    Throwable cause = e.getCause();
                    if (cause instanceof Exception) {
                        throw (Exception) cause;
                    } else if (cause instanceof Error) {
                        throw (Error) cause;
                    } else {
                        throw new RuntimeException(cause);
                    }
                }
            } catch (final Exception e) {
                // always treat other exceptions
                throw e;
            }
        }
    }
}
