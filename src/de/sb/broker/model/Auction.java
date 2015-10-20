package de.sb.broker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
public class Auction extends BaseEntity{

	@Size(min=1,max=255)
	private String title;
	private short unitCount;
	@Min(0)
	private long askingPrice;
	private long closureTimestamp;
	@Size(min=1,max=8189)
	private String description;
	@ManyToOne
	private Person seller;
	@OneToMany
	private List<Bid> bids = new ArrayList<Bid>();
	

	public Person getSeller() {
		return seller;
	}
	
	public long getSellerReference() {
		return seller.getIdentity();
	}

	public List<Bid> getBids() {
		return bids;
	}
	
	public boolean isClosed(){
		return new Date().getTime() > closureTimestamp;
	}
	
	public boolean isSealed(){
		return isClosed() | bids.size() > 0;
	}
	
	//default constructor
	public Auction(){}
}
