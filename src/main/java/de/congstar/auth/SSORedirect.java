package de.congstar.auth;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SSORedirect {
	
	private Logger logger = LogManager.getLogger();
	private final String appname = ConfigurationHolder3.getConfiguration().getString("appconfigitems.sso.appname", "postbox");
	
	public void getRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
		

		String success = request.getRequestURL().toString();
		String queryString = request.getQueryString();
			
		if (success==null) {
			logger.warn("Request URL not set. Setting default request URL");
			success = ConfigurationHolder3.getConfiguration().getString("url.success","http://cis.congstar.local/csmp/overview.html");
		}
			
		if (!request.getScheme().matches("https"))
			success=success.replaceFirst("http", "https");
			
		String url = ConfigurationHolder3.getConfiguration().getString(
				"url.sso", "https://sso.congstar.net/sso/login");

		if (queryString!=null && queryString.length()>2)
			success+="?"+queryString;
				

		logger.debug("Setting callback URL to: " + URLEncoder.encode(success, "UTF-8"));

		success = URLEncoder.encode(success, "UTF-8");
			

		response.sendRedirect(url + "?type=jwt&goto=" + success + "&caller=" +appname);
		return;
	}


}
