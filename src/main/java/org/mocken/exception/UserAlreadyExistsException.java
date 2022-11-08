package org.mocken.exception;

public class UserAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3853137308545906119L;

	public UserAlreadyExistsException() {
		super();
	}
	
	public UserAlreadyExistsException(String s) {
		super(s);
	}
}
