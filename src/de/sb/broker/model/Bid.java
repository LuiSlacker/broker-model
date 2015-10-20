package de.sb.broker.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

@Entity
public class Bid extends BaseEntity{
	
	@Min(0)
	private long price;
	@ManyToOne
	private Auction auction;
	@ManyToOne
	private Person bidder;
	
	
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
	
	//default constructor
	public Bid(){}
	
}