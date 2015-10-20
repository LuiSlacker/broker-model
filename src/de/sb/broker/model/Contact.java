package de.sb.broker.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Contact {

	@Size(min=1,max=63)
	@Pattern(regexp="[a-z_0-9.-]+@[a-z_0-9.-]+/.[a-z]{2,3}", flags=Pattern.Flag.CASE_INSENSITIVE)
	private String email;
	@Max(value=63)
	private String phone; 
}
