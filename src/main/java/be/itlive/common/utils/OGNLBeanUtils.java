package be.itlive.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * This class is used to access and modify fields value in a given object using OGNL expressions ({@link https://commons.apache.org/proper/commons-ognl} ).</p>
 * Consider an instance of class <b>A</b> which contains an attribute <i>b</i> of class <b>B</b> which itself contains an attribute <i>c</i> of class <b>C</b>.
 * <p>To retrieve the value of the field <i>c</i> from <b>A</b>, the expression must have the following form : '<i>b</i>.<i>c</i> ' :
 * <ul><li><code>OGNLBeanUtils.getValue(a, "b.c");</code></li></ul>
 * </p>
 * <p>To set a value <i>v</i> in the field <i>c</i> from <b>A</b>, it's the same form :
 * <ul><li><code>OGNLBeanUtils.setValue(a, "b.c", v);</code></li></ul>
 * </p>
 * @author vbiertho
 * @author vbiertho
 */
public final class OGNLBeanUtils {

    private static final Logger LOGGER = Logger.getLogger(OGNLBeanUtils.class);

    private static final Pattern EXP_PATTERN = Pattern.compile("^(?:[a-zA-Z][a-zA-Z0-9]*\\.?[a-zA-Z][a-zA-Z0-9]*)*$");

    private OGNLBeanUtils() {
    }

    /**
     * This method returns a List of values contained in the given object for the given expressions.
     * @param inObject the object.
     * @param ognlExpressions the OGNL expressions.
     * @return a list of values for the given object and expressions.
     */
    public static List<Object> getValuesList(final Object inObject, final String[] ognlExpressions) {
        return getValuesList(inObject, Arrays.asList(ognlExpressions));
    }

    /**
     * This method returns a Map of values contained in the given object for the given expressions.
     * @param inObject the object.
     * @param ognlExpressions the OGNL expressions.
     * @return a map of values for the given object and expressions.
     */
    public static Map<String, Object> getValuesMap(final Object inObject, final String[] ognlExpressions) {
        return getValuesMap(inObject, Arrays.asList(ognlExpressions));
    }

    /**
     * @param inObject the object.
     * @param ognlExpressions the OGNL expressions.
     * @return a list of values for the given object and expressions.
     */
    public static List<Object> getValuesList(final Object inObject, final List<String> ognlExpressions) {

        List<Object> valuesList = new LinkedList<Object>();

        for (String exp : ognlExpressions) {
            valuesList.add(getValue(inObject, exp));
        }

        return valuesList;
    }

    /**
     * @param inObject the object.
     * @param ognlExpressions the OGNL expressions.
     * @return a map of values for the given object and expressions.
     */
    public static Map<String, Object> getValuesMap(final Object inObject, final List<String> ognlExpressions) {

        Map<String, Object> valuesMap = new HashMap<String, Object>();

        for (String exp : ognlExpressions) {
            valuesMap.put(exp, getValue(inObject, exp));
        }

        return valuesMap;
    }

    /**
     * The expression exp must respect the regex pattern ^(?:[a-zA-Z][a-zA-Z0-9]*\.?[a-zA-Z][a-zA-Z0-9]*)*$.
     * @param inObject the object.
     * @param ognl the OGNL expression.
     * @return a value for the given object and expression.
     */
    public static Object getValue(final Object inObject, final String ognl) {

        String[] expressions = validateAndSplit(ognl);

        Object value = inObject;

        for (int i = 0; i < expressions.length; i++) {
            value = getFieldOrMethodReturnValue(value, expressions[i]);
        }

        return value;
    }

    /**
     * @param object the object.
     * @param ognl the OGNL expression.
     * @return the class for the given object and expression.
     */
    public static Class<?> getType(final Object object, final String ognl) {

        String[] expressions = validateAndSplit(ognl);

        Object value = object;

        for (int i = 0; i < expressions.length; i++) {

            if (i == expressions.length - 1) {

                Field field = recursiveGetField(value, expressions[i]);

                if (field != null) {

                    return field.getType();

                } else {

                    Method method = recursiveGetMethod(value, fieldToGetter(expressions[i]));

                    if (method != null) {
                        return method.getReturnType();
                    }
                }
            }

            value = getFieldOrMethodReturnValue(value, expressions[i]);

        }

        return null;
    }

    /**
     * @param inObject the object.
     * @param ognl the OGNL expression.
     * @param inValue the value.
     * @return the object after assigning the given value.
     */
    public static Object setValue(final Object inObject, final String ognl, final Object inValue) {

        try {

            String[] expressions = validateAndSplit(ognl);

            Object child = inObject;
            Object parent = null;
            Field field = null;

            for (int i = 0; i < expressions.length; i++) {

                field = recursiveGetField(child, expressions[i]);

                parent = child;

                child = field != null ? FieldUtils.readField(field, child, true) : null;
            }

            FieldUtils.writeField(field, parent, inValue, true);

            return inObject;

        } catch (final Exception e) {
            LOGGER.warn(e);
        }

        return inObject;
    }

    /**
     * @param inObject the object.
     * @param inFieldName the field name.
     * @return the field or method return value for the given object and expression.
     */
    public static Object getFieldOrMethodReturnValue(final Object inObject, final String inFieldName) {

        Object value = getFieldValue(inObject, inFieldName);

        if (value == null) {

            value = getMethodReturnValue(inObject, fieldToGetter(inFieldName));
        }

        return value;
    }

    /**
     * @param inObject the object.
     * @param inFieldName the field name.
     * @return the field value for the given object and field name.
     */
    public static Object getFieldValue(final Object inObject, final String inFieldName) {

        try {

            Field field = recursiveGetField(inObject, inFieldName);

            if (field != null) {

                return FieldUtils.readField(field, inObject, true);
            }

        } catch (final Exception e) {
            LOGGER.warn(e);
        }

        return null;
    }

    /**
     * @param inObject the object.
     * @param inMethodName the method name.
     * @param inArgs the arguments.
     * @return the method return value for the given object, method name and arguments.
     */
    public static Object getMethodReturnValue(final Object inObject, final String inMethodName, final Object... inArgs) {
        try {
            return MethodUtils.invokeMethod(inObject, inMethodName, inArgs);
        } catch (final Exception e) {
            LOGGER.warn(e);
        }
        return null;
    }

    /**
     * @param inObject the object.
     * @param inFieldName the field name.
     * @return the field for the given object and field name.
     */
    private static Field recursiveGetField(final Object inObject, final String inFieldName) {

        if (inObject == null) {
            return null;
        }

        Class<?> clazz = inObject.getClass();

        Field field = null;

        do {

            try {
                field = clazz.getDeclaredField(inFieldName);
            } catch (final NoSuchFieldException e) {
            } finally {
                clazz = clazz.getSuperclass();
            }

        } while (clazz != null);

        return field;
    }

    /**
     * @param inObject the object.
     * @param inMethodName the method name.
     * @param inParameterTypes the parameter types.
     * @return the method for the given object, method name and parameter types.
     */
    private static Method recursiveGetMethod(final Object inObject, final String inMethodName, final Class<?>... inParameterTypes) {

        if (inObject == null) {
            return null;
        }

        Class<?> clazz = inObject.getClass();

        Method method = null;

        do {
            try {
                method = clazz.getDeclaredMethod(inMethodName, inParameterTypes);
            } catch (final NoSuchMethodException e) {
            } finally {
                clazz = clazz.getSuperclass();
            }

        } while (clazz != null);

        return method;
    }

    /**
     * @param inName the field name.
     * @return the getter for the given field name.
     */
    private static String fieldToGetter(final String inName) {
        return "get" + inName.substring(0, 1).toUpperCase() + inName.substring(1);
    }

    /**
     * @param expression to validate and split.
     * @return the split expression.
     */
    private static String[] validateAndSplit(final String expression) {

        Matcher matcher = EXP_PATTERN.matcher(expression);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("The expression : \"" + expression + "\" is not valid.");
        }

        return expression.split("\\.");
    }
}
