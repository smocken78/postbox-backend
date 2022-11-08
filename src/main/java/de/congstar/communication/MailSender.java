package de.congstar.communication;

import java.util.Properties;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.exception.ApplicationException;
import org.mocken.init.jobs.EmailTemplateLoader;



/**
 * 
 * @author smocken
 * This class handles Emails and transports them to a configured
 * SMTP Gateway which can be configured using the following parametes
 * mail.recipients => All recipients for emails delimiter is ;
 * mail.sender => The sender address
 * mail.smtp.host => IP/Hostname of the SMTP Server
 * mail.smtp.port => Port on which the SMTP Server listens
 */

public class MailSender {
	
	private String sender = ConfigurationHolder3.getConfiguration().getString("appconfigitems.mail.sender", "no-reply@congstar.net");
	private String smtpHost = ConfigurationHolder3.getConfiguration().getString("appconfigitems.mail.smtp.host", "172.25.0.14");
	private String smtpTLS = ConfigurationHolder3.getConfiguration().getString("appconfigitems.mail.smtp.starttls.enable","false");
	private int smtpPort = ConfigurationHolder3.getConfiguration().getInt("appconfigitems.mail.smtp.port",2501);
	private boolean smtpStarttls = ConfigurationHolder3.getConfiguration().getBoolean("appconfigitems.mail.smtp.ssl", true);
	private int connectionTimeout = ConfigurationHolder3.getConfiguration().getInteger("appconfigitems.mail.smtp.connetiontimeout", 5000);
	private int timeout = ConfigurationHolder3.getConfiguration().getInteger("appconfigitems.mail.smtp.timeout", 10000);
	private String smtpUser = ConfigurationHolder3.getConfiguration().getString("appconfigitems.mail.smtp.user", "test");
	private String smtpPassword = ConfigurationHolder3.getConfiguration().getString("appconfigitems.mail.smtp.password", "test");
	private static Logger logger = LogManager.getLogger(MailSender.class);

	public void email(String recipient) throws ApplicationException 
	{	
				
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			if ("true".equals(smtpTLS)) {
				props.put("mail.smtp.starttls.enable", smtpTLS);
			}
			Session s;
			if (smtpStarttls) {
				logger.debug("Starting TLS session with Mailserver: {} on port: {}",smtpHost,smtpPort);
		        props.put("mail.smtp.starttls.enable","true");
		        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtps.timeout", timeout);
				props.put("mail.smtps.connectiontimeout", connectionTimeout);
			}
			else {
		        props.put("mail.smtp.starttls.enable","false");
				props.put("mail.smtp.timeout", timeout);
				props.put("mail.smtp.connectiontimeout", connectionTimeout);
				
			}
			if (ConfigurationHolder3.getConfiguration().getBoolean("appconfigitems.mail.smtp.authenticate", false)) 
			{
				props.put("mail.smtp.auth", "true");
				logger.debug("Authentication required for Mailserver. Using user {}",smtpUser);
				s = Session.getInstance(props,new MailAuthenticator(smtpUser,smtpPassword));
			}
			else {
				 s = Session.getInstance(props,null);
			}
			MimeMessage message = new MimeMessage(s);
			InternetAddress from = new InternetAddress(sender);
			message.setFrom(from);
			final InternetAddress to = new InternetAddress(recipient);
			message.addRecipient(MimeMessage.RecipientType.TO, to);

			message.setSubject(EmailTemplateLoader.getSubject());
			message.setContent(EmailTemplateLoader.getEmail(), "text/html;charset=utf-8");
			
			Transport.send(message);
		}
		
		catch (AddressException e) {
			// TODO Auto-generated catch block
			logger.error("Address field invalid.",e);
			throw new ApplicationException("Beim Versenden der Email ist ein Fehler aufgetreten");
		} 
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			logger.error("Error sending email", e);
			throw new ApplicationException("Beim Versenden der Email ist ein Fehler aufgetreten");
		}
	}

}
