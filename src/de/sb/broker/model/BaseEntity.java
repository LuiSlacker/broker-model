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
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType 
@XmlSeeAlso({Person.class, Bid.class, Auction.class})
@Table(name="BaseEntity", schema="broker")
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="discriminator", discriminatorType = DiscriminatorType.STRING)
public class BaseEntity implements Comparable<BaseEntity>{

	@XmlID
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long identity;
	
	@XmlElement
	@Column(nullable=false, insertable=false, updatable=true)
	@Min(1)
	@Version
	private int version;
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=false)
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

	@XmlElement
	public int getVersion() {
		return version;
	}

	@XmlElement
	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	
}
