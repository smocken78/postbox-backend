package org.mocken.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.communication.MailSender;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.database.statements.SQLStatementsPostbox;
import org.mocken.s3.S3PostboxWriter;

import jakarta.mail.Address;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailProcessor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private String notificationHeaderName = ConfigurationHolder3.getConfiguration().getString("postbox.notification.header", "X-Notify");
	
	public void run(String fname, String message) {
		
		FileMetaData metaData = new FileMetaData(fname, 2);
		
		try {
			
			Session s = Session.getInstance(new Properties());
			InputStream is = new ByteArrayInputStream(message.getBytes());
			MimeMessage mimeMessage = new MimeMessage(s, is);
			Address [] ias = mimeMessage.getAllRecipients();
			metaData.setCustomerEmail(ias[0].toString());
			metaData.setSubject(mimeMessage.getSubject());
			metaData.setDocumentDateEpochMS(System.currentTimeMillis());
			if (mimeMessage.getContentType().startsWith("text")) {
				try {
					metaData.setPreviewContent( ((String)mimeMessage.getContent()).substring(0, 80));
				}
				catch (Exception e) {
					logger.warn("Exception: ",e);
				}
			}
			else {	
				try {
					MimeMultipart mp = (MimeMultipart)mimeMessage.getContent();
					for (int i=0;i<mp.getCount();i++) {
						if (mp.getContentType().startsWith("text"))
							metaData.setPreviewContent( ((String)mimeMessage.getContent()).substring(0, 80));
					}
				}
				catch (Exception e) {
					logger.warn("Exception: ",e);
				}
			}

			if (is.markSupported()) {
				is.reset();
			}
			else {
				logger.warn("Marking InputStream is not supported, creating new one");
				is = new ByteArrayInputStream(message.getBytes());
			}
			
			S3PostboxWriter writer = new S3PostboxWriter();
			writer.saveFile(fname, is);
			SQLStatementsPostbox sql = new SQLStatementsPostbox();
			sql.addEntry(metaData);
			
			if (mimeMessage.getHeader(notificationHeaderName)!=null &&	Boolean.parseBoolean(mimeMessage.getHeader(notificationHeaderName)[0].trim()))
			{
				MailSender email = new MailSender();
				email.email(ias[0].toString());
			}
		}
		catch (Exception e) {
			logger.error("",e);
		}
		
	}

}
