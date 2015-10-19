package de.sb.broker.model;

@Entity
public class Bid extends BaseEntity{
	
	private long price;
	private Auction auction;
	private Person bidder;
	
	//default constructor
	public Bid(){
		
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
	
	
}