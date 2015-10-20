package de.sb.broker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BaseEntity implements Comparable<BaseEntity>{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long identity;
	
	private int version;
	private long creationTimestamp;
	
	@Override
	public int compareTo(BaseEntity o) {
		if (identity == o.getIdentity()){
			return 0;
		} else if (identity < o.getIdentity()){
			return -1;
		} else{
			return 1;
		}
	}
	
	public long getIdentity() {
		return identity;
	}

	public int getVersion() {
		return version;
	}

	public long getCreationTimestamp() {
		return creationTimestamp;
	}
	
	//default constructor
	public BaseEntity() {}
	
}
