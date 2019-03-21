package be.itlive.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class allowing to cast an object or a list of object to a defined type.
 *
 * @author vbiertho
 */
public final class CastUtils {

	private CastUtils() {
	}

	/**
	 * Cast all objects present in the list to the specified type and put them in a new list.
	 * 
	 * @param      <T> the generic type
	 * @param list the list of objects to cast
	 * @param type the Class to cast the objects to
	 * @return the casted elements, null is given list is null or empty list if given list is empty.
	 * @throws NullPointerException if list is null or list is not empty but type is null
	 * @throws ClassCastException   if an object of the list cannot be cast to type
	 */
	public static <T> List<T> cast(final List<?> list, final Class<T> type) throws NullPointerException, ClassCastException {

		List<T> result = new ArrayList<>();
		for (Object object : list) {
			result.add(cast(object, type));
		}

		return result;
	}

	/**
	 * Cast an object to a specified type.
	 * 
	 * @param        <T> the generic type
	 * @param object the object to cast
	 * @param type   the Class to cast the object to
	 * @return the casted element or null if object is null
	 * @throws NullPointerException if type is null
	 * @throws ClassCastException   if object cannot be cast to type
	 */
	public static <T> T cast(final Object object, final Class<T> type) throws NullPointerException, ClassCastException {
//        if (type == null) {
//            throw new IllegalArgumentException("Type to cast to cannot be null.");
//        }

		return type.cast(object);
	}
}
