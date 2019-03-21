package be.itlive.common.utils;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.itlive.common.enums.MimeType;

/**
 * This class allows to send e-mail.
 *
 * @author vbiertho
 */
public class MailSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

	private Session session;

	/**
	 * The sender address.
	 */
	private String sender;

	/**
	 * The name of the sender.
	 */
	private String senderName = "";

	/**
	 * The list of recipients.
	 */
	private String[] recep;

	/**
	 * The list of carbon copy recipients.
	 */
	private String[] cc;

	/**
	 * The list of blind carbon copy recipients.
	 */
	private String[] bcc;

	/**
	 * The list of replyTo address.
	 */
	private String[] replyTo;

	/**
	 * The content type.
	 */
	private String contentType;

	/**
	 * the subject.
	 */
	private String subject;

	/**
	 * the message.
	 */
	private StringBuilder message = new StringBuilder();

	/**
	 * buffers for attachment.
	 * 
	 * @deprecated use MailAttachment object in place of
	 */
	@Deprecated
	private Map<String, StringBuilder> buffers;

	/**
	 * the MailAttachment list.
	 */
	private List<MailAttachment> mailAttachments;

	/**
	 * @param inSession mail session
	 * @param sender    sender
	 * @param recep     recep
	 */
	public MailSender(final Session inSession, final String sender, final String recep) {
		this(inSession, sender, recep, null, null);
	}

	/**
	 * @param inSession   the session
	 * @param sender      the sender
	 * @param recep       the recipients
	 * @param contentType the content type
	 */
	public MailSender(final Session inSession, final String sender, final String recep, final String contentType) {
		this(inSession, sender, recep, null, contentType);
	}

	/**
	 * @param inSession   the session
	 * @param sender      the sender
	 * @param recep       the recipients
	 * @param replyTo     the replyTo(s)
	 * @param contentType the content type. If null ==> the content type is "text/plain".
	 */
	public MailSender(final Session inSession, final String sender, final String recep, final String replyTo, final String contentType) {
		super();
		session = inSession;
		this.sender = sender;
		this.recep = recep.split(",");
		if (replyTo != null) {
			this.replyTo = replyTo.split(",");
		}
		this.contentType = contentType == null ? MimeType.TEXT_PLAIN.getMimeType() + "; charset=\"UTF-8\"" : contentType;
	}

	/**
	 * Constructor by default.
	 */
	public MailSender() {
		super();
	}

	/**
	 * @return the message
	 */
	public StringBuilder getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final StringBuilder message) {
		this.message = message;
	}

	/**
	 * @return the buffers
	 * @deprecated use getMailAttachments() in place of
	 */
	@Deprecated
	public Map<String, StringBuilder> getBuffers() {
		return buffers;
	}

	/**
	 * @param buffers the buffers to set
	 * @deprecated use setMailAttachments(final List<MailAttachment> mailAttachments) in place of
	 */
	@Deprecated
	public void setBuffers(final Map<String, StringBuilder> buffers) {
		this.buffers = buffers;
	}

	/**
	 * @category Accessor
	 * @return the mailAttachments
	 */
	public List<MailAttachment> getMailAttachments() {
		return mailAttachments;
	}

	/**
	 * @category Accessor
	 * @param mailAttachments the mailAttachments to set
	 */
	public void setMailAttachments(final List<MailAttachment> mailAttachments) {
		this.mailAttachments = mailAttachments;
	}

	/**
	 * @throws MessagingException           if problems of Messaging
	 * @throws UnsupportedEncodingException if problems of UnsupportedEncoding
	 */
	public void sendMail() throws MessagingException, UnsupportedEncodingException {
		Message msg = new MimeMessage(session);

		if (!isBlank(subject)) {
			msg.setSubject(subject);
		}

		if (!isBlank(sender)) {
			if (!isBlank(senderName)) {
				msg.setFrom(new InternetAddress(sender, getSenderName()));
			} else {
				msg.setFrom(new InternetAddress(sender));
			}
		}

		if (recep != null) {
			for (String r : recep) {

				if (!isBlank(r)) {
					msg.addRecipient(RecipientType.TO, new InternetAddress(r));
				}
			}
		}

		if (replyTo != null) {
			Collection<Address> replyToCollection = CollectionUtils.collect(Arrays.asList(replyTo), new Transformer<String, Address>() {
				@Override
				public Address transform(final String input) {
					try {
						return new InternetAddress(input);
					} catch (final AddressException e) {
						throw new IllegalArgumentException("Problems ReplyTo email address", e);
					}
				}
			});
			msg.setReplyTo(replyToCollection.toArray(new InternetAddress[replyTo.length]));
		}

		if (cc != null) {
			for (String r : cc) {
				if (!isBlank(r)) {
					msg.addRecipient(RecipientType.CC, new InternetAddress(r));
				}
			}
		}

		if (bcc != null) {
			for (String r : bcc) {
				if (!isBlank(r)) {
					msg.addRecipient(RecipientType.BCC, new InternetAddress(r));
				}
			}
		}

		MimeMultipart multipart = new MimeMultipart();
		multipart.addBodyPart(createBody());

		// TODO: remove creation of attachment by buffers, we must use MailAttachment object (wma)
		if (null != buffers && buffers.size() > 0) {
			for (BodyPart part : createAttachments()) {
				multipart.addBodyPart(part);
			}
		} else if (null != mailAttachments && mailAttachments.size() > 0) {
			addMailAttachments(multipart);
		} else if (null != buffers && buffers.size() > 0 && null != mailAttachments && mailAttachments.size() > 0) {
			// TODO: remove this when creation of attachment by buffers will be removed (wma)
			throw new IllegalStateException("error when creating attachments.");
		}

		msg.setContent(multipart);

		Transport.send(msg);
	}

	/**
	 * @return message body
	 * @throws MessagingException if problems of messaging
	 */
	protected BodyPart createBody() throws MessagingException {
		MimeBodyPart main = new MimeBodyPart();
		main.setContent(message.toString(), getContentType());
		return main;
	}

	/**
	 * @return attachment body collection
	 * @throws MessagingException if problems of messaging
	 * @deprecated this method will be removed and we must use createMailAttachments()
	 */
	@Deprecated
	// TODO: remove this method and use addMailAttachments()
	protected Collection<? extends BodyPart> createAttachments() throws MessagingException {
		if (null != buffers) {
			Collection<MimeBodyPart> attachments = new ArrayList<>();
			for (Map.Entry<String, StringBuilder> entry : buffers.entrySet()) {
				String filename = entry.getKey();
				String text = entry.getValue().toString();

				MimeBodyPart part = new MimeBodyPart();
				part.setContent(text, MimeType.TEXT_PLAIN.getMimeType());
				part.setDisposition(Part.ATTACHMENT);
				part.setFileName(filename);

				attachments.add(part);
			}
			return attachments;
		}
		return null;
	}

	/**
	 * Add mailAttachements if any to given multipart.
	 * 
	 * @param multipart multipart the multipart to add eventual mailAttachements to
	 * @throws MessagingException if problems of messaging
	 */
	protected void addMailAttachments(final MimeMultipart multipart) throws MessagingException {
		if (multipart != null && mailAttachments != null) {
			for (MailAttachment mailAttachment : mailAttachments) {
				try {
					MimeBodyPart attachPart = new MimeBodyPart();
					attachPart.attachFile(mailAttachment.getFileName());
					multipart.addBodyPart(attachPart);
				} catch (final IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param inSender the sender to set
	 */
	public void setSender(final String inSender) {
		sender = inSender;
	}

	/**
	 * @return the recep
	 */
	public String[] getRecep() {
		return recep;
	}

	/**
	 * @param inRecep the recep to set
	 */
	public void setRecep(final String... inRecep) {
		recep = inRecep;
	}

	/**
	 * @return the cc
	 */
	public String[] getCc() {
		return cc;
	}

	/**
	 * @param cc the cc to set
	 */
	public void setCc(final String... cc) {
		this.cc = cc;
	}

	/**
	 * @return the bcc
	 */
	public String[] getBcc() {
		return bcc;
	}

	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(final String... bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param inSubject the subject to set
	 */
	public void setSubject(final String inSubject) {
		subject = inSubject;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @return main body appendable
	 */
	public Appendable getMessageAppendable() {
		return message;
	}

	/**
	 * @param filename filename
	 * @return attachment appendable
	 */
	public Appendable getFileAppendable(final String filename) {
		if (filename != null) {
			return buffers.get(filename);
		}
		return null;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName the senderName
	 */
	public void setSenderName(final String senderName) {
		this.senderName = senderName;
	}

	/**
	 * @param exception the exception to get the reason for
	 * @return the reason.
	 */
	public static String getReason(final Exception exception) {
		if (exception != null) {
			String detailMessage = exception.getMessage();
			if (StringUtils.isEmpty(detailMessage)) {
				Throwable cause = exception.getCause();
				detailMessage = cause != null ? cause.toString() : exception.getClass().getName();
			}
			return detailMessage;
		}
		return " - ";
	}

	/**
	 * @param exception the exception to get the stackTrace from
	 * @return the strackTrace.
	 */
	public static String getStackTrace(final Exception exception) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		if (exception != null) {
			exception.printStackTrace(pw);
		}

		return sw.toString().replace(System.getProperty("line.separator"), "<br/>");
	}

	/**
	 * @param xml the xml
	 * @return the xml formatted to be inserted in html mail.
	 */
	public static String getXml(final String xml) {
		return StringUtils.replace(StringUtils.replace(xml, "<", "&lt;"), ">", "&gt;");
	}

}
