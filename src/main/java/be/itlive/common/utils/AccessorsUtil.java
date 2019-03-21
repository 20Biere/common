package be.itlive.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.itlive.common.exceptions.ServiceException;

/**
 *
 * This class allow you to test automatically the setters and getters of a class.
 * The fields with primitive types are set with random values while other fields are set with <code>null</code>.
 * If you want to skip some field (because you have only a setter or only a getter or if it's a custom type) you can provide
 * a {@link List} of {@link Field} to skip.
 *
 * @author vbiertho
 *
 */
public class AccessorsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessorsUtil.class);

    private Map<String, Object> parameters = null;

  //@formatter:off
    private Object getRandomValue(final Class<?> inClass) {
        Object value = null;
        if (String.class.equals(inClass)) {
            value = RandomStringUtils.randomAscii(25);
    /*
     * Dates.
     */
        } else if (Date.class.equals(inClass)) {
            value = new Date(RandomUtils.nextLong());
       /*
        * Java 8.
        *
        *  } else if (LocalDate.class.equals(inClass)) {
            value = LocalDate.ofEpochDay(RandomUtils.nextLong());
           } else if (LocalDateTime.class.equals(inClass)) {
            value = LocalDateTime.ofEpochSecond(RandomUtils.nextLong(), 0, ZoneOffset.UTC);
        */
    /*
     * Primitive types and linked Number representation.
     */
        } else if (Boolean.class.equals(inClass) || boolean.class.equals(inClass)) {
            value = RandomUtils.nextBoolean();
        } else if (Double.class.equals(inClass) || double.class.equals(inClass)) {
            value = RandomUtils.nextDouble();
        } else if (Integer.class.equals(inClass) || int.class.equals(inClass)) {
            value = RandomUtils.nextInt(Integer.MAX_VALUE - 1);
        } else if (Long.class.equals(inClass) || long.class.equals(inClass)) {
            value = RandomUtils.nextLong();
        } else if (Short.class.equals(inClass) || short.class.equals(inClass)) {
            value = Integer.valueOf(RandomUtils.nextInt(Short.MAX_VALUE - 1)).shortValue();
        } else if (Float.class.equals(inClass) || float.class.equals(inClass)) {
            value = Double.valueOf(RandomUtils.nextDouble()).floatValue();
        } else if (Byte.class.equals(inClass) || byte.class.equals(inClass)) {
            value = Integer.valueOf(RandomUtils.nextInt(Byte.MAX_VALUE)).byteValue();
        } else if (BigDecimal.class.equals(inClass)) {
            value = BigDecimal.valueOf(RandomUtils.nextLong());
        } else if (BigInteger.class.equals(inClass)) {
            value = BigInteger.valueOf(RandomUtils.nextInt());
        } else if (Number.class.equals(inClass)) {
            value = RandomUtils.nextInt(Byte.MAX_VALUE);
        } else {
            LOGGER.warn("No random value defined for : {}", inClass.toString());
        }
        return value;
    }
  //@formatter:on

    /**
     * Set all the fields that have getters & setters of a given object with random values (null for non trivial type) and get them.
     * If a value that it get is different that the value set, the method throw a {@link ServiceException}.
     * @param inObject Instance of the class to test.
     * @param fieldsNameToIgnore List of fields name to skip (no getter or setter, non significant field, ...).
     * If a field has neither getter nor setter there is no need to specify it.
     * @param compareValueLogger If true, we log the values compared.
     * @return True if getters return well the values set.
     * @throws ServiceException
     */
    public boolean testAccessors(final Object inObject, final List<String> fieldsNameToIgnore, final boolean compareValueLogger)
            throws ServiceException {
        if (inObject != null) {
            parameters = new HashMap<String, Object>();
            List<Field> fields = new ArrayList<Field>();
            for (Field field : ReflectionUtils.collectFields(inObject.getClass())) {
                if (fieldsNameToIgnore == null || !fieldsNameToIgnore.contains(field.getName())) {
                    fields.add(field);
                }
            }
            Method[] methods = inObject.getClass().getDeclaredMethods();
            setAllFields(inObject, fields, methods);
            getAllFields(inObject, fields, methods, compareValueLogger);
        }
        return true;
    }

    /**
     * Set all the fields of a given object with random values (null for non trivial type) and get them.
     * If a value that it get is different that the value set, the method throw a {@link ServiceException}.
     * @param inObject Instance of the class to test.
     * @return True if getters return well the values set.
     * @throws ServiceException
     */
    public boolean testAccessors(final Object inObject) throws ServiceException {
        return testAccessors(inObject, null, false);
    }

    /**
     * Set all the fields of a given object with random values (null for non trivial type) and get them.
     * If a value that it get is different that the value set, the method throw a {@link ServiceException}.
     * @param inObject Instance of the class to test.
     * @param fieldsNameToIgnore List of fields name to skip (no getter or setter, non significant field, ...).
     * If a field has neither getter nor setter there is no need to specify it.
     * @return True if getters return well the values set.
     * @throws ServiceException
     */
    public boolean testAccessors(final Object inObject, final List<String> fieldsNameToIgnore) throws ServiceException {
        return testAccessors(inObject, fieldsNameToIgnore, false);
    }

    /**
     * Get all the fields of the object and compare them with the value set.
     * @param inObject The object.
     * @param fields The object's fields.
     * @param methods The object's methods.
     * @param compareValueLogger If true, we log the values compared.
     * @throws ServiceException
     */
    private void getAllFields(final Object inObject, final List<Field> fields, final Method[] methods, final boolean compareValueLogger)
            throws ServiceException {
        try {
            for (Field field : fields) {
                String getterName = StringUtils.join("get", StringUtils.capitalize(field.getName()));
                if (field.getType().equals(boolean.class)) {
                    getterName = StringUtils.join("is", StringUtils.capitalize(field.getName()));
                }
                for (Method m : methods) {
                    if (getterName.equals(m.getName())) {
                        Object expected = parameters.remove(field.getName());
                        Object objectFound = m.invoke(inObject);
                        if (compareValueLogger) {
                            LOGGER.debug("METHOD <{}> : VALUE SET: {} - VALUE GET : {}", m.getName(), expected, objectFound);
                        }
                        if (!ObjectUtils.equals(expected, objectFound)) {
                            throw new ServiceException("For method " + m.getName() + " -- expected <" + expected + "> but was <" + objectFound + ">");
                        }
                        break;
                    }
                }
            }
        } catch (final InvocationTargetException e) {
            throw new ServiceException(e);
        } catch (final IllegalAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Set all the fields of the object with random values.
     * @param inObject The object.
     * @param fields The object's fields.
     * @param methods The object's methods.
     * @throws ServiceException
     */
    private void setAllFields(final Object inObject, final List<Field> fields, final Method[] methods) throws ServiceException {
        try {
            for (Field field : fields) {
                String setterName = StringUtils.join("set", StringUtils.capitalize(field.getName()));
                for (Method m : methods) {
                    if (setterName.equals(m.getName())) {
                        Class<?>[] classOfParameter = m.getParameterTypes();
                        Object parameter = null;
                        if (ArrayUtils.getLength(classOfParameter) == 1) {
                            parameter = getRandomValue(classOfParameter[0]);
                            parameters.put(field.getName(), parameter);
                        }
                        m.invoke(inObject, parameter);
                        break;
                    }
                }
            }
        } catch (final InvocationTargetException e) {
            throw new ServiceException(e);
        } catch (final IllegalAccessException e) {
            throw new ServiceException(e);
        }
    }
}
