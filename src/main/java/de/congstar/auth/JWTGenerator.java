package de.congstar.auth;


import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mocken.configuration.ConfigurationHolder3;
import org.mocken.user.User;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTGenerator  {

	private static Logger logger = LogManager.getLogger(JWTGenerator.class);
	private static final int expirationTimeInMinutes = ConfigurationHolder3.getConfiguration().getInt("appconfig.jwt.expirationTimeInMinutes",300) >= (ConfigurationHolder3.getConfiguration().getInt("appconfig.jwt.cookie_TTL_in_minutes", 240) + 60)? 
			ConfigurationHolder3.getConfiguration().getInt("appconfig.jwt.expirationTimeInMinutes",300) :
			(ConfigurationHolder3.getConfiguration().getInt("appconfig.jwt.cookie_TTL_in_minutes", 240) + 60);
	
	public static String getJWTToken(User user) {
		
		try {
			String secretString = ConfigurationHolder3.getConfiguration().getString("appconfig.jwt.secretKey","8K#Uu0N:8x$r6E6-WZ!");
			
			Signer signer = HMACSigner.newSHA512Signer(secretString);
			
			JWT jwt = new JWT().setIssuer("confact.congstar.net")
	                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
	                .setSubject("SSO validated user")
	                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(expirationTimeInMinutes));


			jwt.addClaim("user", user.getUsername());
			jwt.addClaim("email", user.getEmail());
			jwt.addClaim("first_name", user.getFirstName());
			jwt.addClaim("last_name", user.getLastName());
			

			return JWT.getEncoder().encode(jwt, signer);
		}
		catch (Exception e) {
			logger.error("",e);
		}

		return null;

	}
	

	

}
