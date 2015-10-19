package de.sb.broker.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Auction extends BaseEntity{

	private String title;
	private short unitCount;
	private long askingPrice;
	private long closureTimestamp;
	private String description;
	private Person seller;
	private Set<Bid> bids;
	
	public Auction(){
	}

	public Person getSeller() {
		return seller;
	}
	
	public long getSellerReference() {
		return seller.getIdentity();
	}

	public Set<Bid> getBids() {
		return bids;
	}
	
	public boolean isClosed(){
		return new Date().getTime() > closureTimestamp;
	}
	
	public boolean isSealed(){
		return bids.size() > 0;
	}
}
