package org.mocken.email;

import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.mail.util.BASE64DecoderStream;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailResponseParser {
	
	private Logger logger = LogManager.getLogger(this.getClass());

	public JSONArray parse(InputStream is) {
		
		JSONArray jar = new JSONArray();
		try {
			Session s = Session.getInstance(new Properties());
			MimeMessage mimeMessage = new MimeMessage(s, is);
			if (mimeMessage.getContentType().startsWith("text")) {
				try {
					JSONObject json = new JSONObject();
					json.put("content",(String)mimeMessage.getContent());
					json.put("content-type",mimeMessage.getContentType());
					jar.put(json);
				}
				catch (Exception e) {
					logger.warn("Exception: ",e);
				}
			}
			else {		
				try {
					MimeMultipart mp = (MimeMultipart)mimeMessage.getContent();
					for (int i=0;i<mp.getCount();i++) {
						JSONObject json = new JSONObject();
						if (mp.getBodyPart(i).getContentType().indexOf("text")>-1) {
							json.put("content",(String)mp.getBodyPart(i).getContent());
							json.put("content-type",mp.getBodyPart(i).getContentType());
						}
						else if (mp.getBodyPart(i).getContentType().indexOf("octet-stream")>-1) {
							BASE64DecoderStream ds = (BASE64DecoderStream)(mp.getBodyPart(i).getContent());
							String content = Base64.getEncoder().encodeToString(ds.readAllBytes());
							json.put("content",content);
							json.put("content-type",mp.getBodyPart(i).getContentType());
						}
	
						jar.put(json);
					}
				}
				catch (Exception e) {
					logger.warn("Exception: ",e);
				}
			}

			
		}
		catch (Exception e) {
			logger.error("",e);
		}
		
		return jar;
	}

}
