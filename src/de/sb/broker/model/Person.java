package de.sb.broker.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
public class Person extends BaseEntity{

	//fields
	@Column
	private String alias;
	@Column
	private byte[] passwordHash;
	@Embedded @Valid
	private Name name;
	@Embedded @Valid
	private Contact contact;
	@Embedded @Valid
	private Address address;
	@Column @Enumerated
	private Group userRole;
	
	//relations
	@OneToMany
	private Set<Auction> auctions = new HashSet<Auction>();
	@OneToMany
	private Set<Bid> bids = new HashSet<Bid>();
	
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
