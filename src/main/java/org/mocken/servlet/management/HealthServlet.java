package org.mocken.servlet.management;

import java.io.PrintWriter;

import org.json.JSONObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/management/health")
public class HealthServlet extends HttpServlet{

	private static final long serialVersionUID = -2082538637502792137L;
	

	public void service (HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		json.put("status","UP");
		json.put("message", "Everything seems to be fine");
		
		try {
			PrintWriter wr = response.getWriter();
			wr.write(json.toString());
			wr.close();
		}
		catch (Exception e) {
			//ignore
		}
	}
}
