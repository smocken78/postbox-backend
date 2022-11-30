package org.mocken.servlet.mail;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.email.EmailProcessor;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/api/incomingEMail")
public class IncomingEmail extends HttpServlet {

	private static final long serialVersionUID = 1717947941937912111L;
	private Logger logger = LogManager.getLogger(this.getClass());

	
	public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		try {
			String s = request.getParameter("email");
			if (s!=null) {
				EmailProcessor ep= new EmailProcessor(); 
				ep.run(s);
			}
			else {
				logger.warn("Called without email....");
			}
		}
		catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}
	

	public void doGet (HttpServletRequest request, HttpServletResponse response) 
	{
		
		try {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
		catch (Exception e) {
			logger.error("",e);
		}
		
	}

	

}
