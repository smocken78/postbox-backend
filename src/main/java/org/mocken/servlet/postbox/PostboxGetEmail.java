package org.mocken.servlet.postbox;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.mocken.database.statements.SQLStatementsPostbox;
import org.mocken.email.EmailResponseParser;
import org.mocken.s3.S3PostboxReader;
import org.mocken.user.User;

import com.amazonaws.services.s3.model.S3Object;


@WebServlet(urlPatterns = ("/service/postboxEmail"))
public class PostboxGetEmail extends HttpServlet {
    private static final long serialVersionUID = 5652268371771279360L;
    private Logger logger = LogManager.getLogger(getClass());

    public void service(HttpServletRequest request, HttpServletResponse response) {

        try {

        	String filename = request.getParameter("filename");
        	User user = (User)request.getAttribute("user");
            SQLStatementsPostbox sql = new SQLStatementsPostbox();
            if (filename != null && sql.validateGetFile(user.getEmail(),filename)) {
            	S3PostboxReader reader = new S3PostboxReader();
            	S3Object s3Object = reader.getFile(filename);
            	
        		if (s3Object == null) {
    				response.sendError(HttpServletResponse.SC_NOT_FOUND);
    				return;
    			}
        		
        		EmailResponseParser erp = new EmailResponseParser();
        		JSONArray jar = erp.parse(s3Object.getObjectContent());
    		    response.setHeader("Content-Type","application/json");
    		    PrintWriter wr = response.getWriter();
        		wr.write(jar.toString());
    			wr.close();
    		    
            }
            else {
            	response.sendError(HttpServletResponse.SC_NOT_FOUND);
            	return;
            }
        }
        catch (Exception e) {
            logger.error("", e);
        }
    }

}

