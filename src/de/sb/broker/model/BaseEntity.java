package de.sb.broker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class BaseEntity implements Comparable<BaseEntity>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long identity;
	
	@Column
	private int version;
	@Column
	private long creationTimestamp;
	
	//default constructor
	public BaseEntity() {}
	
	@Override
	public int compareTo(BaseEntity o) {
		if (this.identity == o.getIdentity()){
			return 0;
		} else if (this.identity < o.getIdentity()){
			return -1;
		} else{
			return 1;
		}
	}
	
	//Accessors
	public long getIdentity() {
		return identity;
	}

	public int getVersion() {
		return version;
	}

	public long getCreationTimestamp() {
		return creationTimestamp;
	}
}
