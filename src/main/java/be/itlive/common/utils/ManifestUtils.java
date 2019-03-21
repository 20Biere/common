package be.itlive.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;

/**
 * Utilities to get manifest.
 *
 * @author vbiertho
 */
public final class ManifestUtils {

    /**
     * Manifest attribute name <strong>Project</strong>.
     */
    public static final String ATTRIBUTE_NAME_PROJECT = "general/Project";

    /**
     * Gets manifest using <tt>Thread.currentThread().getContextClassLoader()</tt>.
     * @param name manifest's attribute name used to identify the right one.
     * @param value manifest's attribute value associated to <code>name</code>.
     * @return first {@link Manifest} object matching pair <code>name</code>/<code>value</code>, <tt>null</tt> if none found.
     * @throws IOException if an error occurred
     */
    public static Manifest getManifest(final Attributes.Name name, final String value) throws IOException {
        return getManifest(null, name, value);
    }

    /**
     * Gets manifest.
     * @param classLoader {@link ClassLoader} used to get manifest. May be <tt>null</tt>, in that case <tt>Thread.currentThread().getContextClassLoader()</tt> will be used to get current class loader.
     * @param name manifest's attribute name used to identify the right one.
     * @param value manifest's attribute value associated to <code>name</code>.
     * @return first {@link Manifest} object matching pair <code>name</code>/<code>value</code>, <tt>null</tt> if none found.
     * @throws IOException if an error occurred
     */
    public static Manifest getManifest(final ClassLoader classLoader, final Attributes.Name name, final String value) throws IOException {
        if (name == null) {
            throw new NullPointerException("Name parameter cannot be null");
        }
        return getManifest(classLoader, name.toString(), value);
    }

    /**
     * Gets manifest.
     * @param classLoader {@link ClassLoader} used to get manifest. May be <tt>null</tt>, in that case <tt>Thread.currentThread().getContextClassLoader()</tt> will be used to get current class loader.
     * @param project manifest's attribute value associated to {@link #ATTRIBUTE_NAME_PROJECT}.
     * @return first {@link Manifest} object matching pair {@link #ATTRIBUTE_NAME_PROJECT}/<code>project</code>, <tt>null</tt> if none found.
     * @throws IOException if an error occurred
     */
    public static Manifest getManifest(final ClassLoader classLoader, final String project) throws IOException {
        return getManifest(classLoader, ATTRIBUTE_NAME_PROJECT, project);
    }

    /**
     * Gets manifest.
     * @param classLoader {@link ClassLoader} used to get manifest. May be <tt>null</tt>, in that case <tt>Thread.currentThread().getContextClassLoader()</tt> will be used to get current class loader.
     * @param name manifest's attribute name used to identify the right one. If <code>name</code> contains a <strong>/</strong>, the name before <strong>/</strong> is the name of the
     * attributes group and the name after is the name of the pair. If no <strong>/</strong> in <code>name</code>, get the pair from main attributes.
     * @param value manifest's attribute value associated to <code>name</code>.
     * @return first {@link Manifest} object matching pair <code>name</code>/<code>value</code>, <tt>null</tt> if none found.
     * @throws IOException if an error occurred
     */
    public static Manifest getManifest(ClassLoader classLoader, final String name, final String value) throws IOException {
        if (name == null || value == null) {
            throw new NullPointerException("String parameters cannot be null");
        }
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        Enumeration<URL> urls = classLoader.getResources("META-INF/MANIFEST.MF");
        Manifest manifest = null;
        while (manifest == null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            InputStream is = url.openStream();
            manifest = new Manifest(is);
            String title = getValue(manifest, name);
            if (!StringUtils.contains(title, value)) {
                manifest = null;
            }
        }
        return manifest;
    }

    /**
     * Gets manifest using <tt>Thread.currentThread().getContextClassLoader()</tt>.
     * @param project manifest's attribute value associated to {@link #ATTRIBUTE_NAME_PROJECT}.
     * @return first {@link Manifest} object matching pair {@link #ATTRIBUTE_NAME_PROJECT}/<code>project</code>, <tt>null</tt> if none found.
     * @throws IOException if an error occurred
     */
    public static Manifest getManifest(final String project) throws IOException {
        return getManifest((ClassLoader) null, project);
    }

    /**
     * Gets manifest using <tt>Thread.currentThread().getContextClassLoader()</tt>.
     * @param name manifest's attribute name used to identify the right one.
     * @param value manifest's attribute value associated to <code>name</code>. If <code>name</code> contains a <strong>/</strong>, the name before <strong>/</strong> is the name of the
     * attributes group and the name after is the name of the pair. If no <strong>/</strong> in <code>name</code>, get the pair from main attributes.
     * @return first {@link Manifest} object matching pair <code>name</code>/<code>value</code>, <tt>null</tt> if none found.
     * @throws IOException if an error occurred
     */
    public static Manifest getManifest(final String name, final String value) throws IOException {
        return getManifest(null, name, value);
    }

    /**
     * Gets value from manifest.
     * @param manifest {@link Manifest}.
     * @param name name of the pair name/value. If <code>name</code> contains a <strong>/</strong>, the name before <strong>/</strong> is the name of the attributes group and the
     * name after is the name of the pair. If no <strong>/</strong> in <code>name</code>, get the pair from main attributes.
     * @return the value associated to <code>name</code>.
     */
    private static String getValue(final Manifest manifest, final String name) {

        String value = null;

        if (StringUtils.contains(name, "/")) {

            try {
                String[] strings = name.split("/");
                Attributes attributes = manifest.getAttributes(strings[0]);
                value = attributes.getValue(strings[1]);
            } catch (final Exception e) {
                value = null;
            }

        } else {
            value = manifest.getMainAttributes().getValue(name);
        }

        return value;

    }

    /**
     * @category Constructor
     */
    private ManifestUtils() {
        super();
    }

}
