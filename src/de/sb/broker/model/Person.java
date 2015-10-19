package de.sb.broker.model;

import java.util.HashSet;
import java.util.Set;

public class Person extends BaseEntity{

	//fields
	private Name name;
	private Contact contact;
	private Address address;
	
	//relations
	private Set auctions;
	
	public Person() {
		name = new Name();
		contact = new Contact();
		address = new Address();
		
		auctions = new HashSet<Auction>();
	}
	
	public static enum Group {
		ADMIN, USER
	}
}
