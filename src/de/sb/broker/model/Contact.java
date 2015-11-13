package de.sb.broker.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Contact {

	@Size(min=1,max=63)
	@Pattern(regexp="[a-z_0-9.-]+@[a-z_0-9.-]+\\.[a-z]{2,3}", flags=Pattern.Flag.CASE_INSENSITIVE)
	@Column(nullable=false, insertable=true, updatable=true)
	@NotNull
	private String email;
	
	@Size(max=63)
	@Column(nullable=true, insertable=true, updatable=true)
	private String phone; 
	
	//default constructor
	public Contact(){
		this.phone = "";
	}

	//Accessors
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
