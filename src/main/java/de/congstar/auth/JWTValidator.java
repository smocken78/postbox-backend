package de.congstar.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.exception.ApplicationException;
import org.mocken.exception.AuthenticationException;
import org.mocken.user.User;


public class JWTValidator {

    private User user = null;
    private Logger logger = LogManager.getLogger(this.getClass());

    public boolean authenticate (HttpServletRequest request, String encodedJWT) throws ApplicationException, AuthenticationException {

		var decrypted = decryptor(encodedJWT); 
		var json = decrypted.getJson();
		
		if (json==null || json.isEmpty())
			throw new ApplicationException("Unauthorized");
		
		user = new User(json.path("user").asText(),
				        json.path("email").asText(),
				        json.path("first_name").asText(),
				        json.path("last_name").asText());
		
		
		request.setAttribute("user", user);	
		return decrypted.isRecreateToken();
	}

	
	private JWTResponse decryptor (String encodedJWT) 
	{
		JWTResponse jwtResponse = null;
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode json = mapper.createObjectNode();
		String secretString = ConfigurationHolder3.getConfiguration().getString("appconfig.jwt.secretKey","8K#Uu0N:8x$r6E6-WZ!");
		long tokenRecreationTimeInMillis = (ConfigurationHolder3.getConfiguration().getLong("appconfig.jwt.cookie_TTL_in_minutes", 240) - 60L) * 60L * 1000L;
		try {				
			Verifier verifier = HMACVerifier.newVerifier(secretString);
			JWT jwt = JWT.getDecoder().decode(encodedJWT, verifier);
			boolean recreateToken = true;
			try {
				recreateToken = jwt.expiration.toInstant().toEpochMilli()>System.currentTimeMillis()- tokenRecreationTimeInMillis;
			}
			catch (Exception e) {
				//ignore
			}
			json = mapper.readTree(jwt.toString());
			jwtResponse = new JWTResponse(recreateToken, json);
		}
		catch (JWTExpiredException e) {
		    // accept expiry as normal
		}
		catch (Exception e) {
			logger.warn(e.getMessage(),e);
		}
			
		return jwtResponse;
		
	}
	
	private class JWTResponse {
		private final boolean recreateToken;
		private final JsonNode json;
		
		public JWTResponse(boolean recreateToken, JsonNode json) {
			this.json = json;
			this.recreateToken = recreateToken;
			// TODO Auto-generated constructor stub
		}

		public boolean isRecreateToken() {
			return recreateToken;
		}

		public JsonNode getJson() {
			return json;
		}
	}


}
