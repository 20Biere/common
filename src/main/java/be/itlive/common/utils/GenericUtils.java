package be.itlive.common.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Utility class to work with generics.
 * @author vbiertho
 */
public final class GenericUtils {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(GenericUtils.class);

    /**
     * .
     */
    private GenericUtils() {

    }

    /**
     * Return the class of a generic Collection property in a know class.<br/>
     * Collection&lt;String> return String.class
     * @param ownerClass the class that own the propery.
     * @param propertyName property in the entity
     * @return generic class
     * @throws NoSuchFieldException if propertyName not found
     */
    public static Class<?> getGenericCollectionType(final Class<?> ownerClass, final String propertyName) throws NoSuchFieldException {
        Field field = ownerClass.getDeclaredField(propertyName);
        return getGenericCollectionType(field);
    }

    /**
     * Return the class of a generic Collection property in a know class.<br/>
     * Collection&lt;String> return String.class
     * @param field Field.
     * @return class class.
     * @throws ClassCastException if generic is a Type (T)
     */
    public static Class<?> getGenericCollectionType(final Field field) throws ClassCastException {
        try {
            Type gtype = field.getGenericType();
            if (gtype instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) gtype;
                Type[] targs = ptype.getActualTypeArguments();
                Class<?> c = (Class<?>) targs[0];

                return c;
            } else {
                LOGGER.warn("getGenericType is not a ParameterizedType!" + gtype.toString());
            }
            return null;
        } catch (final SecurityException e) {

            e.printStackTrace();
            return null;
        } catch (final java.lang.ClassCastException e) {
            LOGGER.info("TypeVariable (T) typed collection. You should use returnedClass if available");
            throw e;
        }
    }

    /**
     * Get the underlying class for a type, or null if the type is a variable type.
     * @param type the type
     * @return the underlying class
     */
    public static Class<?> getClass(final Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get the actual type arguments a child class has used to extend a generic base class.
     * @param <T> type.
     * @param baseClass the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    public static <T> List<Class<?>> getTypeArguments(final Class<T> baseClass, final Class<? extends T> childClass) {
        Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (type != null && !getClass(type).equals(baseClass)) {
            if (type instanceof Class<?>) {
                // there is no useful information for us in raw types, so just keep going.
                type = ((Class<?>) type).getGenericSuperclass();
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass, determine (if possible)
        // the raw class for that type argument.
        Type[] actualTypeArguments;
        if (type instanceof Class<?>) {
            actualTypeArguments = ((Class<?>) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }

    /**
     * Search for in the class hierarchy to find a Generic Super class, and return the typed argument<br/>
     * Given Activity extends AbstractDescriptable&lt;String> : will return String.<br/>
     * Given OrganisationalUnit extends OUOrNot extends AbstractDescriptable&lt;Long> while return Long.
     * @param inClass the parameterized class.
     * @param inIndex the index of the parameter.
     * @return the class of the parameter.
     */
    public static Class<?> getTypeArgumentAsClass(final Class<?> inClass, final int inIndex) {
        Class<?> clazz = inClass;
        Type genericSuperclass = clazz.getGenericSuperclass();
        while (!(genericSuperclass instanceof ParameterizedType)) {
            genericSuperclass = ((Class<?>) genericSuperclass).getGenericSuperclass();
        }
        return (Class<?>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[inIndex];
    }

}
