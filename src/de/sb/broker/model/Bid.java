package de.sb.broker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Bid extends BaseEntity{
	
	@Column(nullable=false, insertable=true, updatable=true)
	@Min(1)
	@NotNull
	private long price;
	
	@ManyToOne 
	@JoinColumn(name="auctionReference")
	@Column(nullable=false, insertable=true, updatable=false)
	private Auction auction;
	
	@ManyToOne 
	@JoinColumn(name="bidderReference")
	@Column(nullable=false, insertable=true, updatable=false)
	private Person bidder;
	
	//default constructor
	protected Bid(){
		this(null, null);
	}
	
	public Bid(Auction auction, Person bidder){
		this.price = 1;
		this.auction = auction;
		this.bidder = bidder;
	}
	
	public Auction getAuction() {
		return auction;
	}
	
	public long getAuctionReference() {
		return auction.getIdentity();
	}
	
	public Person getBidder() {
		return bidder;
	}
	
	public long getBidderReference() {
		return bidder.getIdentity();
	}
	
	public long getPrice() {
		return price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
}