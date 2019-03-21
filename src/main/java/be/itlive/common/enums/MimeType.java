package be.itlive.common.enums;

/**
 * Represent the MIME type a file can be of.
 *
 * @author vbiertho
 */
public enum MimeType {

    /**
     * application/javascript.
     */
    APPLICATION_JAVASCRIPT("application/javascript"),

    /**
     * "application/pdf".
     */
    APPLICATION_PDF("application/pdf"),

    /**
     * application/xhtml+xml.
     */
    APPLICATION_XHTML_XML("application/xhtml+xml"),

    /**
     * application/json.
     */
    APPLICATION_JSON("application/json"),

    /**
     * application/xml.
     */
    APPLICATION_XML("application/xml"),

    /**
     * application/zip.
     */
    APPLICATION_ZIP("application/zip"),

    /**
     * application/x-excel.
     */
    APPLICATION_X_EXCEL("application/x-excel"),

    /**
     * image/gif.
     */
    IMAGE_GIF("image/gif"),

    /**
     * image/jpeg.
     */
    IMAGE_JPEG("image/jpeg"),

    /**
     * image/png.
     */
    IMAGE_PNG("image/png"),

    /**
     * multipart/mixed.
     */
    MULTIPART_MIXED("multipart/mixed"),

    /**
     * multipart/alternative.
     */
    MULTIPART_ALTERNATIVE("multipart/alternative"),

    /**
     * multipart/related.
     */
    MULTIPART_RELATED("multipart/related"),

    /**
     * text/css.
     */
    TEXT_CSS("text/css"),

    /**
     * text/csv.
     */
    TEXT_CSV("text/csv"),

    /**
     * text/html.
     */
    TEXT_HTML("text/html"),

    /**
     * text/javascript.
     */
    TEXT_JAVASCRIPT("text/javascript"),

    /**
     * text/plain.
     */
    TEXT_PLAIN("text/plain"),

    /**
     * text/xml.
     */
    TEXT_XML("text/xml");

    private String mimeType;

    MimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return the MIME type value.
     */
    public String getMimeType() {
        return mimeType;
    }
}
