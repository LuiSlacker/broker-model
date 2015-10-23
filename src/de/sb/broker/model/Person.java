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
	@OneToMany(mappedBy="Auction")
	private Set<Auction> auctions;
	@OneToMany(mappedBy="Bid")
	private Set<Bid> bids;
	
	//default constructor
	public Person() {}
	
	public Person(String alias, String password, Name name, Contact contact, 
		   Address address, Group group) throws PasswordHashException {
		this.alias = alias;
		this.passwordHash = Person.passwordHash(password);
		this.name = name;
		this.contact = contact;
		this.address = address;
		this.userRole = group;
		
		this.auctions = new HashSet<Auction>();
		this.bids = new HashSet<Bid>();
	}
	
	public static enum Group {
		ADMIN, USER
	}
	
	public Group getGroup(){
		return userRole;
	}
	
	public static byte[] passwordHash(String password) throws PasswordHashException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new PasswordHashException("Password hashing failed");	
		}
		md.update(password.getBytes());
		return  md.digest();
	}
}
