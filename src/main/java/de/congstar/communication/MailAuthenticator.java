package de.congstar.communication;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
	
	private String username;
	private String password;
	
	public MailAuthenticator (String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public PasswordAuthentication getPasswordAuthentication () {
		return new PasswordAuthentication(username,password);
		
	}

}