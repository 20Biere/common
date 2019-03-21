package be.itlive.common.enums;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for MimeType.
 * @author vbiertho
 *
 */
public class MimeTypeTest {

    @Test
    public void testMimeType_uniqueValues() throws Exception {
        List<String> mimeTypes = new ArrayList<String>();

        for (MimeType mimeType : MimeType.values()) {
            if (mimeTypes.contains(mimeType.getMimeType())) {
                fail("MimeType '" + mimeType.getMimeType() + "' is not unique");
            } else {
                mimeTypes.add(mimeType.getMimeType());
            }
        }
    }

    /**
     * Compares mimeType name and value ignoring non alphanumeric characters (ex : APPLICATION_XHTML_XML vs application/xhtml+xml)
     * @throws Exception
     */
    @Test
    public void testMimeType_nameAndMimeTypeCorrespond() throws Exception {
        for (MimeType mimeType : MimeType.values()) {
            String name = mimeType.name().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            String mimeTypeString = mimeType.getMimeType().replaceAll("[^a-zA-Z0-9]", "");
            assertEquals(name, mimeTypeString);
        }
    }
}
