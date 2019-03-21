package be.itlive.common.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.itlive.common.exceptions.ServiceException;

/**
 * Utility class to work with Reflection.
 *
 * @author vbiertho
 */
public final class ReflectionUtils {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    private ReflectionUtils() {
    }

    /**
     * Returns an empty list if none are found.
     * @param c class
     * @return field of class and superclass
     */
    public static List<Field> collectFields(Class<?> c) {
        final List<Field> fields = new LinkedList<>();
        do {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        } while (c != null);

        return fields;
    }

    /**
     * Return list of all field name in given class, excluding given names.
     * @param c a class.
     * @param nameToExclude name to exclude.
     * @return set of fieldName.
     */
    public static Set<String> collectFieldsdName(final Class<?> c, final String... nameToExclude) {
        final Set<String> fields = new HashSet<>();
        List<Field> collectedFields = collectFields(c);
        List<String> toExclude = Arrays.asList(nameToExclude);
        for (Field field : collectedFields) {
            String fieldName = field.getName();
            if (!toExclude.contains(fieldName)) {
                fields.add(fieldName);
            }
        }

        return fields;
    }

    /**
     * Get all the fields which are annotated with the given annotation. Returns an empty list if none are found
     * @param c class
     * @param annotations annotations
     * @return List<Field>
     */
    public static List<Field> collectFieldsByAnnotation(final Class<?> c, final Class<? extends Annotation>... annotations) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = c; clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                for (Class<? extends Annotation> annotation : annotations) {
                    if (field.isAnnotationPresent(annotation)) {
                        fields.add(field);
                        break;
                    }
                }
            }
        }

        return fields;
    }

    /**
     * Get all the fields which are annotated with the given annotation. Returns an empty Map if none are found
     * @param c class
     * @param annotations annotations
     * @return Map<fileName, Field>.
     */
    public static Map<String, Field> collectFieldsByAnnotationAsMap(final Class<?> c, final Class<? extends Annotation>... annotations) {
        final Map<String, Field> fields = new HashMap<>();
        for (Class<?> clazz = c; clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                for (Class<? extends Annotation> annotation : annotations) {
                    if (field.isAnnotationPresent(annotation)) {
                        fields.put(field.getName(), field);
                        break;
                    }
                }
            }
        }

        return fields;
    }

    /**
     * Build a map of fields where the key is the field name. Returns an empty Map if none are found
     * @param c Class
     * @return Map<fileName, Field>.
     */
    public static Map<String, Field> collectFieldsAsMap(Class<?> c) {
        final Map<String, Field> fields = new HashMap<>();
        do {
            for (Field f : c.getDeclaredFields()) {
                fields.put(f.getName(), f);
            }
            c = c.getSuperclass();
        } while (c != null);

        return fields;
    }

    /**
     * ex :collectFields(C.class, Modifier.STATIC + Modifier.PRIVATE,Modifier.TRANSIENT) will ignore all private static
     * and all transient field .
     * @param c class
     * @param modifersToExclude See {@link java.lang.reflect.Modifier}
     * @return field of class and superclass
     */
    public static List<Field> collectFields(Class<?> c, final int... modifersToExclude) {
        final List<Field> fields = new LinkedList<>();
        do {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        } while (c != null);

        Iterator<Field> it = fields.iterator();
        allField: while (it.hasNext()) {
            Field f = it.next();
            for (int modifiers : modifersToExclude) {
                if ((f.getModifiers() & modifiers) == modifiers) {
                    it.remove();
                    continue allField;
                }
            }
        }

        return fields;
    }

    /**
     * Build a map of fields where the key is the field name.
     * @param c Class
     * @param modifersToExclude See {@link java.lang.reflect.Modifier}.
     * @return Map<fileName, Field>.
     */
    public static Map<String, Field> collectFieldsAsMap(final Class<?> c, final int... modifersToExclude) {
        final Map<String, Field> fields = collectFieldsAsMap(c);
        Iterator<Map.Entry<String, Field>> it = fields.entrySet().iterator();
        allField: while (it.hasNext()) {
            Map.Entry<String, Field> entry = it.next();
            for (int modifiers : modifersToExclude) {
                if ((entry.getValue().getModifiers() & modifiers) == modifiers) {
                    it.remove();
                    continue allField;
                }
            }
        }

        return fields;
    }

    /**
     * ex :collectFields(C.class, Modifier.STATIC + Modifier.PRIVATE,Modifier.TRANSIENT) will ignore all private static.
     * and all transient field {
     * @param c class
     * @param fieldType fieldType
     * @return field of class and superclass
     */
    public static List<Field> collectFields(Class<?> c, final Class<?>... fieldType) {
        final List<Field> fields = new LinkedList<>();
        do {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        } while (c != null);

        Iterator<Field> it = fields.iterator();
        allField: while (it.hasNext()) {
            Field f = it.next();
            for (Class<?> clazz : fieldType) {
                if (clazz.isInterface()) {
                    if (!clazz.isAssignableFrom(f.getType())) {
                        it.remove();
                        continue allField;
                    }
                } else {
                    if (!f.getType().equals(clazz)) {
                        it.remove();
                        continue allField;
                    }
                }
            }
        }

        return fields;
    }

    /**
     * Get, and init with an ArrayList if null, a collection property from an entity.
     * @param entityTo entity holding the collection field
     * @param collectionField the field.
     * @return not null collection
     * @throws Exception if pbm
     */
    public static Collection<?> initCollection(final Serializable entityTo, final Field collectionField) throws Exception {
        Collection<?> collection = (Collection<?>) PropertyUtils.getProperty(entityTo, collectionField.getName());
        if (collection == null) {
            collection = new ArrayList<>();
        }

        return collection;
    }

    /**
     * Get, and init with an ArrayList if null, a collection property from an entity.
     * @param fieldType Collection, Map, Set, List
     * @return not null iterable.
     */
    public static Iterable<?> initIterableType(final Class<?> fieldType) {
        Iterable<?> object;
        if (Set.class.isAssignableFrom(fieldType)) {
            object = new HashSet<>();
        } else if (Map.class.isAssignableFrom(fieldType)) {
            object = (Collection<?>) new HashMap<>();
        } else {
            object = new ArrayList<>();
        }

        return object;
    }

    /**
     * Get a list of classes present in specified package. Find every package in the current classLoader (included in
     * Jar file).
     * @param pckgname the fully-qualified package name
     * @param recurse if <code>true</code>, recurse into sub-packages
     * @return a List of classes
     * @throws ClassNotFoundException if a class problem occurs
     * @throws ServiceException when something critical append (initiate a rollback).
     */
    public static List<Class<? extends Object>> getClasses(final String pckgname, final boolean recurse)
            throws ClassNotFoundException, ServiceException {
        ArrayList<Class<? extends Object>> classes = new ArrayList<>();
        // Get a File object for the package
        File directory = null;
        URL resource = null;
        String path = pckgname.replace('.', '/');
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }

            resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }

        } catch (final NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " (" + directory + ") does not appear to be a valid package");
        }

        List<String> subPackages = null;

        // if (recurse) {
        subPackages = new ArrayList<>();
        // }
        if (resource.getProtocol().equals("jar")) {
            // JarURLConnection jar = (JarURLConnection)resource.openConnection();
            JarInputStream jarFile = null;
            try {
                JarURLConnection jar = (JarURLConnection) resource.openConnection();
                jarFile = new JarInputStream(new FileInputStream(new File(jar.getJarFile().getName())));
                JarEntry jarEntry;
                while ((jarEntry = jarFile.getNextJarEntry()) != null) {
                    if (jarEntry.getName().startsWith(path)) {
                        if (jarEntry.getName().endsWith(".class")) {

                            String fullClassName = jarEntry.getName().replaceAll("/", "\\.");
                            Class<? extends Object> c = Class.forName(fullClassName.substring(0, fullClassName.length() - 6));
                            classes.add(c);

                        } else if (jarEntry.isDirectory()) {
                            String packageName = jarEntry.getName().substring(0, jarEntry.getName().length() - 1).replaceAll("/", "\\.");
                            if (!packageName.equals(pckgname)) {
                                subPackages.add(jarEntry.getName());
                            }
                        }
                    }
                }
            } catch (final Exception x) {
                throw new ServiceException(x);
            } finally {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (final IOException e) {
                        throw new ServiceException(e);
                    }
                }
            }
        } else {
            directory = new File(resource.getFile());
            if (directory.exists()) {
                // Get the list of the files contained in the package
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {

                    String filename = files[i].getName();
                    // we are only interested in .class files
                    if (filename.endsWith(".class")) {
                        // removes the .class extension
                        classes.add(Class.forName(pckgname + '.' + filename.substring(0, filename.length() - ".class".length())));
                    } else if (recurse && files[i].isDirectory()) {
                        subPackages.add(pckgname + '.' + filename);
                    }
                }
            } else {
                throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
            }

            if (recurse) {
                for (String pckg : subPackages) {
                    List<Class<? extends Object>> subClasses = getClasses(pckg, true);
                    classes.addAll(subClasses);
                }
            }
        }
        return classes;
    }

    public static List<Object> collectConstantsForClass(final Class<?> clazz) throws ServiceException {
//FIXME VBI : constant are static final, not only static.
        try {
            List<Object> listConstants = new ArrayList<>();

            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    Object o = FieldUtils.readStaticField(f);
                    listConstants.add(o);
                }
            }

            return listConstants;
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * NullSafe & force Accessibility.
     * @param field Target Field.
     * @param target Target Object.
     * @return Value or null.
     */
    public static Object readField(final Field field, final Object target) {
        if (field == null) {
            return null;
        }
        if (target == null) {
            return null;
        }

        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }

    }

    /**
     * Construct a new instance of a class, using the default constructor, even if private or default.
     * @param clazz class to instantiate
     * @param <T> object type
     * @return a new instance of T.
     * @throws InstantiationException if the class can't be instantiate.
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<T> clazz) throws InstantiationException {
        T value = null;
        if (List.class.isAssignableFrom(clazz)) {
            value = (T) new ArrayList<>();
        } else if (Set.class.isAssignableFrom(clazz)) {
            value = (T) new HashSet<>();
        } else if (Map.class.isAssignableFrom(clazz)) {
            value = (T) new HashMap<>();
        } else {
            try {
                //Try using the default constructor.
                value = clazz.newInstance();
            } catch (final Exception ex) {
                try {
                    //fallback using reflection
                    value = newInstanceReflect(clazz);
                } catch (final InstantiationException e) {
                    LOGGER.error("### Can't instance type {}. {}", clazz.getName(), e.getMessage());
                    throw e;
                }
            }
        }
        return value;
    }

    /**
     * Instantiate a class even if there are no private constructor.
     * <b>Warning</b> use sun package.
     * @param clazz class to instantiate.
     * @param <T> class type.
     * @return instance.
     * @throws InstantiationException when given class can't be configured.
     */
    private static <T> T newInstanceReflect(final Class<T> clazz) throws InstantiationException {
        //Warning: using sun package classes is discouraged! (but no other "simple" solution available)
        try {
            final Constructor<?> constructor = sun.reflect.ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz,
                    Object.class.getDeclaredConstructor());

            return clazz.cast(constructor.newInstance());
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | InstantiationException e) {
            throw new InstantiationException(e.getMessage());
        }

    }

}
