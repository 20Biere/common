package be.itlive.common.exceptions;


/**
 * Encapsulates an <tt>Exception</tt> already handled by <tt>ErrorMailSender</tt>.
 * @author vbiertho
 */
public class AlreadyMailedException extends ServiceException {

    /**
     * @category Property
     */
    private static final long serialVersionUID = 5086395529394084784L;

    /**
     * @category Constructor
     * @param inMsg message.
     * @param inCause handled exception.
     */
    public AlreadyMailedException(final String inMsg, final Throwable inCause) {
        super(inMsg, inCause);
    }

    /**
     * @category Constructor
     * @param inCause handled exception.
     */
    public AlreadyMailedException(final Throwable inCause) {
        super(inCause);
    }

}
