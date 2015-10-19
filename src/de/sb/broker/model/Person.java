package de.sb.broker.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Entity
public class Person extends BaseEntity{

	//fields
	private String alias;
	private byte[] passwordHash;
	private Name name;
	private Contact contact;
	private Address address;
	private Group userRole;
	
	//relations
	private Set<Auction> auctions;
	private Set<Bid> bids;
	
	//default constructor
	public Person() {
	}
	
	public static enum Group {
		ADMIN, USER
	}
	
	public Group getGroup(){
		return userRole;
	}
	
	public static byte[] passwordHash(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());
		return  md.digest();
	}
}
