package de.sb.broker.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

@Embeddable
public class Name {

	@XmlElement
	@Column(name="familyName", nullable=false, insertable=true, updatable=true)
	@Size(min=1,max=31)
	@NotNull
	private String family;
	
	@XmlElement
	@Column(name="givenName", nullable=false, insertable=true, updatable=true)
	@Size(min=1,max=31)
	@NotNull
	private String given;

	//default constructor
	protected Name(){}
	
	//Accessors
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGiven() {
		return given;
	}

	public void setGiven(String given) {
		this.given = given;
	}
}
