package org.mocken.init;

import java.util.logging.Logger;

import org.mocken.database.connectors.HikariDataSourceManager;
import org.mocken.init.jobs.EmailTemplateLoader;
import org.mocken.init.jobs.InitializeLog4j;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


@WebListener
public class InitializationServlet implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		
		System.setProperty("mail.mime.base64.ignoreerrors", "true");
		Logger.getAnonymousLogger().info("Starting initialization process");

		Logger.getAnonymousLogger().info("Initializing logging framework log4j");
		InitializeLog4j log4j = new InitializeLog4j();
		log4j.init();
		Logger.getAnonymousLogger().info("Initialized logging framework log4j");	
		
		Logger.getAnonymousLogger().info("Initializing Hikari DataSources");
		HikariDataSourceManager.initializePools();
		Logger.getAnonymousLogger().info("Initialized Hikari DataSources");	
		
		Logger.getAnonymousLogger().info("Initializing Email Template");
		EmailTemplateLoader.getEmail();
		Logger.getAnonymousLogger().info("Initialized Email Template");	

		Logger.getAnonymousLogger().info("Finished initialization process");
	
	}
	
	public void contextDestroyed(ServletContextEvent sce) {
		HikariDataSourceManager.shutDownPools();
		EmailTemplateLoader.destroy();
	}
	
}
