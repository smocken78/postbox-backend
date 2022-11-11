package org.mocken.init.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;


public class EmailTemplateLoader {
	
	static Logger logger = LogManager.getLogger(EmailTemplateLoader.class);
	private static final Timer timer = new Timer();
	private static String NOTIFICATON_TEMPLATE = "";
	
	static {
		new EmailTemplateLoader();
	}

	private EmailTemplateLoader() {
		start();
	}
	
	public static String getEmail() {
		return NOTIFICATON_TEMPLATE;
	}
	
	public static String getSubject() {
		return "Neue Naricht in Deinem Postfach!";
	}
	
	private void start() {
		timer.schedule(new TimerTask () {

			@Override
			public void run() {
				ThreadedLoadTemplate job = new ThreadedLoadTemplate();
				NOTIFICATON_TEMPLATE = job.run();

			}			
			
		}, 0, 1800000L );	
	}

	public static void destroy() {
		timer.cancel();
	}
}


class ThreadedLoadTemplate  {
	
		
	public ThreadedLoadTemplate() {
		// TODO Auto-generated constructor stub
	}

	public String run() {
		String file = ConfigurationHolder3.getConfiguration().getEncodedString("postbox.notification.template", null);
		if (file!=null) 
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)),"UTF-8"));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line=br.readLine())!=null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();
			}
			catch (Exception e) {
				EmailTemplateLoader.logger.error("",e);
			}

		return "";
		
	}

}