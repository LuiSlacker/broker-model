package de.sb.broker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
public class Auction extends BaseEntity{

	@Column
	@Size(min=1,max=255)
	private String title;
	@Column
	@Min(0)
	private short unitCount;
	@Column
	@Min(0)
	private long askingPrice;
	@Column
	private long closureTimestamp;
	@Column
	@Size(min=1,max=8189)
	private String description;
	@ManyToOne
	@JoinColumn(name="") //TODO
	private Person seller;
	@OneToMany(mappedBy="Bid")
	private List<Bid> bids;
	
	//default constructor
	public Auction(){}
	
	public Auction(String title, short unitCount, long askingPrice, long closureTimeStamp, 
			String description, Person seller){
		this.title = title;
		this.unitCount = unitCount;
		this.askingPrice = askingPrice;
		this.closureTimestamp = closureTimeStamp;
		this.description = description;
		
		this.seller = seller;
		this.bids = new ArrayList<Bid>();
	}

	public void addBid(Bid bid){
		this.bids.add(bid);
	}
	
	public boolean isClosed(){
		return new Date().getTime() > closureTimestamp;
	}
	
	public boolean isSealed(){
		return isClosed() | bids.size() > 0;
	}
	
	//Accessors
	public Person getSeller() {
		return seller;
	}
	
	public long getSellerReference() {
		return seller.getIdentity();
	}
	
	public List<Bid> getBids() {
		return bids;
	}
}
