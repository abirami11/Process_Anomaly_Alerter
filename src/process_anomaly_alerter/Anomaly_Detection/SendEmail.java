/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter.Anomaly_Detection;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	public static void main(String[] args) {
		Properties props = new Properties();
                final String hostEmail="bellevue148@gmail.com";
                final String hostPassword="";
                String msg = "Hello World!";
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(hostEmail,hostPassword);
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("bellevue148@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("navalk@uw.edu"));
                        message.addRecipients(Message.RecipientType.CC,InternetAddress.parse("svasisht@uw.edu"));
                        message.addRecipients(Message.RecipientType.CC,InternetAddress.parse("swetha91@uw.edu"));
                        message.addRecipients(Message.RecipientType.CC,InternetAddress.parse("abirami@uw.edu"));
                        
                        message.setSubject("Testing Subject");
			message.setText(msg +
					"\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}