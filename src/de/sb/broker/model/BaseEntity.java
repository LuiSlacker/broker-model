package de.sb.broker.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name="BaseEntity", schema="broker")
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="discriminator", discriminatorType = DiscriminatorType.STRING)
public class BaseEntity implements Comparable<BaseEntity>{

	@Column(nullable=false, insertable=false, updatable = false)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@NotNull
	private long identity;
	
	@Column(nullable=false, insertable=false, updatable = false)
	@NotNull
	private int version;
	
	@Column(nullable=false, insertable=true, updatable=false)
	@NotNull
	private long creationTimestamp;
	
	//default constructor
	public BaseEntity() {
		this.creationTimestamp = System.currentTimeMillis();
		this.version = 1;
	}
	
	@Override
	public int compareTo(final BaseEntity entity) {
		return Long.compare(this.getIdentity(), entity.getIdentity());
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
