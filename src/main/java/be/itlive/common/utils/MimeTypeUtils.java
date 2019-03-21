package be.itlive.common.utils;

import org.apache.commons.lang3.StringUtils;

import be.itlive.common.enums.Extension;
import be.itlive.common.enums.MimeType;

/**
 * Utility class exposing business operations to work with some MIME types.
 * 
 * @author vbiertho
 */
public final class MimeTypeUtils {

	private MimeTypeUtils() {
	}

	/**
	 * Retrieve the file MIME type from it's extension.
	 * 
	 * @param fileName the name of the file to retrieve the MIME type of
	 * @return the MIME type of the given file name or null if not found
	 */
	public static MimeType getMimeType(final String fileName) {
		MimeType mimeType = null;

		Extension extension = Extension.getExtensionFromString(StringUtils.substringAfterLast(fileName, "."));
		if (extension != null) {
			switch (extension) {
			case CSV:
				mimeType = MimeType.TEXT_CSV;
				break;
			case PDF:
				mimeType = MimeType.APPLICATION_PDF;
				break;
			case TXT:
				mimeType = MimeType.TEXT_PLAIN;
				break;
			case XLS:
				mimeType = MimeType.APPLICATION_X_EXCEL;
				break;
			default:
				break;
			}
		}

		return mimeType;
	}
}
