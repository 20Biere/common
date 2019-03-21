package be.itlive.common.enums;

/**
 * Represent the extension a file can be of.
 *
 * @author vbiertho
 */
public enum Extension {

    /**
     * PDF extension.
     */
    PDF(".pdf"),

    /**
     * XLS extension.
     */
    XLS(".xls"),

    /**
     * TXT extension.
     */
    TXT(".txt"),

    /**
     * CSV extension.
     */
    CSV(".csv");

    private String value;

    Extension(final String value) {
        this.value = value;
    }

    /**
     * @return the extension value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Find the enum entry corresponding to given value.
     * @param value the value to find
     * @return the corresponding enum entry or null
     */
    public static Extension getExtensionFromString(final String value) {
        for (Extension extension : values()) {
            if (extension.getValue().equals(value) || extension.getValue().equals("." + value)) {
                return extension;
            }
        }
        return null;
    }

    /**
     * @return true if current extension is a PDF, false otherwise
     */
    public boolean isPdf() {
        return this.equals(PDF);
    }

    /**
     * @return true if current extension is a XLS, false otherwise
     */
    public boolean isXls() {
        return this.equals(XLS);
    }

    /**
     * @return true if current extension is a TXT, false otherwise
     */
    public boolean isTxt() {
        return this.equals(TXT);
    }

    /**
     * @return true if current extension is a CSV, false otherwise
     */
    public boolean isCsv() {
        return this.equals(CSV);
    }
}
