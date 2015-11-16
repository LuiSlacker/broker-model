package de.sb.broker.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

import de.sb.java.validation.Inequal;
import de.sb.java.validation.Inequal.Operator;

@Table(name="Auction", schema="broker")
@Entity
@PrimaryKeyJoinColumn(name="auctionIdentity")
@Inequal(leftAccessPath="closureTimestamp", rightAccessPath="creationTimestamp", operator=Operator.GREATER)
public class Auction extends BaseEntity{

	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true, length=255)
	@Size(min=1,max=255)
	@NotNull
	private String title;
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true)
	@Min(1)
	private short unitCount;
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true)
	@Min(1)
	private long askingPrice;
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true)
	private long closureTimestamp;
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true, length=8189)
	@Size(min=1,max=8189)
	@NotNull
	private String description;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="sellerReference", nullable=false, insertable=true, updatable=false)
	private Person seller;
	
	@OneToMany(mappedBy="auction", cascade = CascadeType.REMOVE)
	@NotNull
	private Set<Bid> bids;
	
	//default constructor
	protected Auction () {
		this(null);
	}
	
	public Auction (Person seller){
		this.unitCount = 1;
		this.askingPrice = 1;
		this.seller = seller;
		this.closureTimestamp = System.currentTimeMillis() + 24*60*60*1000;
		this.bids = new HashSet<Bid>();
	}


	public void addBid(Bid bid){
		this.bids.add(bid);
	}
	
	@XmlElement
	public boolean isClosed(){
		return System.currentTimeMillis() > closureTimestamp;
	}
	
	@XmlElement
	public boolean isSealed(){
		return isClosed() | bids.size() > 0;
	}
	
	//Accessors
	public Person getSeller() {
		return seller;
	}
	
	public long getSellerReference() {
		return (this.seller == null) ? 0 : this.seller.getIdentity();
	}
	
	public String getTitle() {
		return title;
	}
	
	public short getUnitCount() {
		return unitCount;
	}
	
	public long getAskingPrice() {
		return askingPrice;
	}
	
	public long getClosureTimestamp() {
		return closureTimestamp;
	}
	
	public String getDescription() {
		return description;
	}
	
	
	public Set<Bid> getBids() {
		return bids;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUnitCount(short unitCount) {
		this.unitCount = unitCount;
	}

	public void setAskingPrice(long askingPrice) {
		this.askingPrice = askingPrice;
	}

	public void setClosureTimestamp(long closureTimestamp) {
		this.closureTimestamp = closureTimestamp;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
