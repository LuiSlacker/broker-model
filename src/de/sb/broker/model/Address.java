package de.sb.broker.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

@Embeddable
public class Address {

	@XmlElement
	@Column(nullable=true, insertable=true, updatable=true)
	@Size(max=63)
	private String street;
	
	@XmlElement
	@Column(nullable=true, insertable=true, updatable=true)
	@Size(max=15)
	private String postcode;
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true)
	@Size(min=1,max=63)
	@NotNull
	private String city;
	
	//default constructor
	public Address(){
		this.street = "";
		this.postcode = "";
	}
	
	//Accessors
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
