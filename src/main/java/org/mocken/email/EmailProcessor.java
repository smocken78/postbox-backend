package org.mocken.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.communication.MailSender;
import org.mocken.database.statements.SQLStatementsPostbox;
import org.mocken.s3.S3PostboxWriter;

import jakarta.mail.Address;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailProcessor {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	public void run(String fname, String message) {
		
		FileMetaData metaData = new FileMetaData(fname, 2);
		
		try {
			
			Session s = Session.getInstance(new Properties());
			InputStream is = new ByteArrayInputStream(message.getBytes());
			MimeMessage mimeMessage = new MimeMessage(s, is);
			Address [] ias = mimeMessage.getAllRecipients();
			metaData.setCustomerEmail(ias[0].toString());
			metaData.setTitle(mimeMessage.getSubject());
			metaData.setDocumentDateEpochMS(System.currentTimeMillis());
			metaData.setContentType("message/rfc822");
			if (mimeMessage.getContentType().startsWith("text")) {
				try {
					logger.debug("MESSAGE content 20 signs: " +  ((String)mimeMessage.getContent()).substring(0, 20));
				}
				catch (Exception e) {
					logger.warn("Exception: ",e);
				}
			}
			else {
				
				try {
					MimeMultipart mp = (MimeMultipart)mimeMessage.getContent();
					logger.debug("MESSAGE content 20 signs: " +  ((String)mp.getBodyPart(0).getContent()).substring(0, 20));
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
			
			if (mimeMessage.getHeader("X-Congstar-Notify")!=null &&	Boolean.parseBoolean(mimeMessage.getHeader("X-Congstar-Notify")[0].trim()))
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
