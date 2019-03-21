package be.itlive.common.comparator;

import java.util.Comparator;

/**
 * Delegates compare to decorated comparator unless an object to compare is <tt>null</tt>.
 * @author vbiertho
 * @param <T> the type of objects that may be compared by this comparator.
 */
public class NullSafeDecoratorComparator<T> implements Comparator<T> {

    private static final int EQUALS = 0;

    private static final int GREATER = 1;

    private static final int LESSER = -1;

    private final Comparator<T> comparator;

    private boolean nullFirst;

    /**
     * @category Constructor
     * @param comparator comparator to decorate.
     */
    public NullSafeDecoratorComparator(final Comparator<T> comparator) {
        this(comparator, false);
    }

    /**
     * @category Constructor
     * @param comparator comparator to decorate.
     * @param nullFirst if <tt>true</tt> <tt>null</tt> will be placed on top, otherwise on tail.
     */
    public NullSafeDecoratorComparator(final Comparator<T> comparator, final boolean nullFirst) {
        super();
        if (comparator == null) {
            throw new NullPointerException();
        }
        this.comparator = comparator;
        this.nullFirst = nullFirst;
    }

    /**
     * {@inheritDoc}
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final T o1, final T o2) {
        int result;
        if (o1 == null && o2 == null || o1 == o2) {
            result = EQUALS;
        } else if (o1 == null) {
            result = nullFirst ? LESSER : GREATER;
        } else if (o2 == null) {
            result = nullFirst ? GREATER : LESSER;
        } else {
            result = comparator.compare(o1, o2);
        }
        return result;
    }

}
