package org.mocken.servlet.postbox;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.user.User;


@WebServlet(urlPatterns = ("/service/addTestEmail"))
public class PostboxAddTestEmail extends HttpServlet {
    private static final long serialVersionUID = 5652268371771279360L;
    private Logger logger = LogManager.getLogger(getClass());

    public void service(HttpServletRequest request, HttpServletResponse response) {

        try {

        	User user = (User)request.getAttribute("user");
        	String notify = request.getParameter("notify");
        	String archive = request.getParameter("archive");
        	
        	
        }
        catch (Exception e) {
            logger.error("", e);
        }
    }

}

