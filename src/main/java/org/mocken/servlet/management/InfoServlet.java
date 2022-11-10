package org.mocken.servlet.management;

import java.io.PrintWriter;
import java.util.jar.Attributes;
import java.util.jar.Manifest;


import org.json.JSONObject;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/management/info","/management/build"})
public class InfoServlet extends HttpServlet{

	private static final long serialVersionUID = -2082538637502792137L;
	

	public void service (HttpServletRequest request, HttpServletResponse response) {
		
		try {
			ServletContext application = getServletConfig().getServletContext();
			Manifest mf = new Manifest(application.getResourceAsStream("/META-INF/MANIFEST.MF"));
			JSONObject enclosing = new JSONObject();
			JSONObject json = new JSONObject();
			
			Attributes attr = mf.getMainAttributes();
			json.put("built_by", attr.getValue("Built-By"));
			json.put("title", attr.getValue("Implementation-Title"));
			json.put("appname", attr.getValue("Implementation-Title"));
			json.put("artifact", attr.getValue("Implementation-Title"));
			json.put("version", attr.getValue("git-revision-id"));
			json.put("build_date", attr.getValue("Compile-Time"));
			json.put("revision_date", attr.getValue("git-revision-time"));
			json.put("build", attr.getValue("git-revision-id"));
			json.put("revision", attr.getValue("git-revision-id"));
			json.put("branch", attr.getValue("git-branch"));

			enclosing.put("build", json);
			PrintWriter wr = response.getWriter();
			wr.write(json.toString());
			wr.close();
		}
		catch (Exception e) {
			//ignore
		}
	}
}
