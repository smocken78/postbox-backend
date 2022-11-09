package org.mocken.auth.jwt;

import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.exception.AuthenticationException;
import org.mocken.user.User;


public class JWTTokenDecoder  {

	private User user = null;
	private Logger logger = LogManager.getLogger(this.getClass());

	public User validate(String encodedJWT) throws AuthenticationException {

		JSONObject json = decryptor(encodedJWT);
		
		if (json==null)
			throw new AuthenticationException("Unauthorized");
			user = new User(json);
			return user;
	}

	
	private JSONObject decryptor (String encodedJWT) 
	{
		
		JSONObject json = null;
		String secretString = ConfigurationHolder3.getConfiguration().getString("appconfig.jwt.secretKey","LJS37842983829!jls.!");
		
		try {
				
			Verifier verifier = HMACVerifier.newVerifier(secretString);
			JWT jwt = JWT.getDecoder().decode(encodedJWT, verifier);
			json = new JSONObject(jwt.getObject("user").toString());
		}
		catch (Exception e) {
			logger.warn(e.getMessage(),e);
		}
			
		return json;
		
	}

	


}
