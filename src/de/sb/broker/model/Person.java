package de.sb.broker.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CacheIndex;

@XmlRootElement(name = "person")
@Table(name="Person", schema="broker")
@Entity
@PrimaryKeyJoinColumn(name="personIdentity")
public class Person extends BaseEntity{

	//fields
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true, unique=true)
	@Size(min=1,max=16)
	@NotNull
	@CacheIndex(updateable=true) //f�r secondlevel cache
	private String alias;
	
	@Column(nullable=false, insertable=true, updatable=true)
	@Size(min=32,max=32)
	@NotNull
	private byte[] passwordHash;
	
	@XmlElement
	@Embedded
	@Valid
	@NotNull
	private Name name;
	
	@XmlElement
	@Embedded
	@Valid
	@NotNull
	private Contact contact;
	
	@XmlElement
	@Embedded
	@Valid
	@NotNull
	private Address address;
	
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name="groupAlias", nullable=false, insertable=true, updatable=true)
	@NotNull
	private Group group;
	
	//relations
	@OneToMany(mappedBy="seller", cascade = CascadeType.REMOVE)
	@NotNull
	private Set<Auction> auctions;
	@OneToMany(mappedBy="bidder", cascade = CascadeType.REMOVE)
	@NotNull
	private Set<Bid> bids;
	
	public Person(){
		this.alias = "";
		this.passwordHash = Person.passwordHash("");
		this.name = new Name();
		this.contact = new Contact();
		this.address = new Address();
		this.auctions = new HashSet<Auction>();
		this.bids = new HashSet<Bid>();
		this.group = Group.USER;
	}
	
	public static enum Group {
		ADMIN, USER
	}
	
	public static byte[] passwordHash(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError();
		}
		md.update(password.getBytes());
		return  md.digest();
	}
	
	public void addAuction(Auction auction){
		this.auctions.add(auction);
	}
	
	public void addBid(Bid bid){
		this.bids.add(bid);
	}
	
	//Accessors
	public Group getGroup(){
		return group;
	}
	
	public void setGroup(Group group){
		this.group = group;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public byte[] getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String password) {
		this.passwordHash = Person.passwordHash(password);
	}

	public Name getName() {
		return name;
	}

	public Contact getContact() {
		return contact;
	}

	public Address getAddress() {
		return address;
	}

	public Set<Auction> getAuctions() {
		return auctions;
	}

	public Set<Bid> getBids() {
		return bids;
	}

}
