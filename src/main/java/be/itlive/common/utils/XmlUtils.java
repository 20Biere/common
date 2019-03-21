package be.itlive.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import be.itlive.common.exceptions.ServiceException;

/**
 * Utility class to perform some actions on an xml String.
 *
 * @author vbiertho
 */
public final class XmlUtils {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class);

    private XmlUtils() {
    }

    /**
     * Pretty print a xml String. Example : <br />
     *
     * This input : <pre>{@code<root><element>lorem</element><element>ipsum</element></root>}
     * </pre>
     * Gives this output : <pre>{@code
     *        <root>
     *            <element>lorem</element>
     *            <element>ipsum</element>
     *        </root> }
     *        </pre>
     * @param unformattedXml The xml to print.
     * @return The formatted xml.
     * @throws ServiceException If an error occured while the XML is transformed.
     */
    public static String prettyPrint(final String unformattedXml) throws ServiceException {
        LOGGER.debug("Entering String prettyPrint(String)");
        LOGGER.trace("Trying to pretty print {}", unformattedXml);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Transformer transformer = SAXTransformerFactory.newInstance().newTransformer();
            // Remove the following string from the result : <?xml version="1.0" encoding="UTF-8"?>.
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            // Indentation ON.
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty("{http://xml.customer.org/xslt}indent-amount", "4");
            Source source = new SAXSource(new InputSource(new ByteArrayInputStream(unformattedXml.getBytes())));
            StreamResult result = new StreamResult(baos);
            transformer.transform(source, result);
            String formatted = new String(((ByteArrayOutputStream) result.getOutputStream()).toByteArray());
            LOGGER.trace("Result of the transformation : {}", formatted);
            LOGGER.debug("Exiting String prettyPrint(String)");
            return formatted;
        } catch (final Exception e) {
            throw new ServiceException(e);
        }
    }

}
