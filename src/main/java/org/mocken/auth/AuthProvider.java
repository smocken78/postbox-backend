package org.mocken.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mocken.auth.jwt.JWTTokenDecoder;
import org.mocken.auth.jwt.JWTTokenEncoder;
import org.mocken.database.statements.LoginStatements;
import org.mocken.exception.AuthenticationException;
import org.mocken.user.User;

public class AuthProvider {
	
	private Logger logger = LogManager.getLogger(this.getClass());
	private User user = null;
	

	public AuthenticationResponse get(String token) {
		
    	logger.debug("Checking token: " + token);
		if (token!=null) {
			try {
				User user = checkJWTToken(token);
				JSONObject json = user.toJSON();
				return new AuthenticationResponse(HttpServletResponse.SC_OK,json);
			}
			catch (AuthenticationException e) {
				return new AuthenticationResponse(HttpServletResponse.SC_UNAUTHORIZED,new JSONObject());
			}
		}
		return new AuthenticationResponse(HttpServletResponse.SC_UNAUTHORIZED,new JSONObject());
		
	}
	
	public AuthenticationResponse post(HttpServletRequest request) {
		
		JSONObject json = new JSONObject();
		String email = request.getParameter("email")!=null?request.getParameter("email").trim():null;
		String password = request.getParameter("password");
		if (email ==null || password ==null) {
			json.put("status", "FAILED");
			return new AuthenticationResponse(HttpServletResponse.SC_UNAUTHORIZED,json);
		}

		LoginStatements login = new LoginStatements();
		user = login.validateLoginData(email, password);
		if (user==null) {
			json.put("status", "FAILED");
			return new AuthenticationResponse(HttpServletResponse.SC_UNAUTHORIZED,json);
		}
		else {
			JWTTokenEncoder jwt = new JWTTokenEncoder();
			String token = jwt.getJWTToken(user);
			json = user.toJSON();
			json.put("token", token);			
			
			return new AuthenticationResponse(HttpServletResponse.SC_OK,json);
		}

	}
	
	public User checkJWTToken(String token) throws AuthenticationException {
		JWTTokenDecoder jwtTokenDecoder = new JWTTokenDecoder();
		return jwtTokenDecoder.validate(token);
	}

}
