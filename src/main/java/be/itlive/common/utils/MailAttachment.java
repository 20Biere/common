package be.itlive.common.utils;

import java.io.Serializable;

import javax.mail.Part;

/**
 * @author vbiertho
 *
 */
public class MailAttachment implements Serializable {

    /**
     * @category Property
     */
    private static final long serialVersionUID = 1L;

    private String fileName;

    private String mimeType;

    private byte[] bytes;

    private String disposition = Part.ATTACHMENT;

    /**
     * @category Constructor
     * @param fileName the file name
     * @param mimeType the mime type
     * @param bytes the bytes
     */
    public MailAttachment(final String fileName, final String mimeType, final byte[] bytes) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.bytes = bytes;
    }

    /**
     * @category Constructor
     * @param fileName the file name
     * @param mimeType the mime type
     * @param bytes the bytes
     * @param disposition the disposition (Part.ATTACHMENT, Part.INLINE)
     */
    public MailAttachment(final String fileName, final String mimeType, final byte[] bytes, final String disposition) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.bytes = bytes;
        this.disposition = disposition;
    }

    /**
     * @category Accessor
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @category Accessor
     * @param fileName the fileName to set
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * @category Accessor
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @category Accessor
     * @param mimeType the mimeType to set
     */
    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @category Accessor
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * @category Accessor
     * @param bytes the bytes to set
     */
    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * @category Accessor
     * @return the disposition
     */
    public String getDisposition() {
        return disposition;
    }

}
