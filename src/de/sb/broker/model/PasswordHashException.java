package de.sb.broker.model;

public class PasswordHashException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public PasswordHashException(){}
	
	public PasswordHashException(String msg){
		super(msg);
	}
}
