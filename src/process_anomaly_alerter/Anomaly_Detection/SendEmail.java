/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process_anomaly_alerter.Anomaly_Detection;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    public  SendEmail(String emailList, String msg, String msgSubject){
		Properties props = new Properties();
                final String hostEmail="no.reply.process.anomaly.alert@gmail.com";
                final String hostPassword="anomaly_alerter";
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
			message.setFrom(new InternetAddress("no.reply.process.anomaly.alert@gmail.com", "Anomaly Detection Tool"));                        
			for(int i =0; i<emailList.split(",").length;i++)
                            message.addRecipients(Message.RecipientType.TO,InternetAddress.parse(emailList.split(",")[i]));
                        
                        message.setSubject(msgSubject);
			message.setText(msg + "\nThanks for using the tools\n");
			//Transport.send(message);

			System.out.println("Email sent for client "+emailList+" "+msgSubject);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
}
