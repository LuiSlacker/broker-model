package de.sb.broker.model;

@Entity
public class BaseEntity implements Comparable<BaseEntity>{

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
	
	//default constructor
	public BaseEntity() {
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
	
	
}
