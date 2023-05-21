package edu.trucktrack.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmailUtil {

	public void sendEmailWithAttachment(String recipient, File attachment) {
		try {
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("bidokfeed@gmail.com", "bcnatxfyylqctnfp"));
			email.setStartTLSEnabled(true);

			email.setFrom("bidokfeed@gmail.com");
			email.addTo(recipient);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setPath(attachment.getPath());
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setDescription("Report");
			emailAttachment.setName(attachment.getName());

			email.attach(attachment);
			email.setSubject("Report");
			email.send();
		} catch (EmailException ex) {
			ex.printStackTrace();
		}
	}

	public void sendMessage(String emailAddress, String message) {
		try {

			MultiPartEmail email = new MultiPartEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("bidokfeed@gmail.com", "bcnatxfyylqctnfp"));
			email.setStartTLSEnabled(true);

			email.setFrom("bidokfeed@gmail.com");
			email.addTo(emailAddress);

			email.setMsg(message);
		} catch (EmailException e) {

		}
	}
}
