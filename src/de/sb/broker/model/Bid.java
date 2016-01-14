package de.sb.broker.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;

import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.jersey.message.filtering.EntityFiltering;

import de.sb.java.validation.Inequal;
import de.sb.java.validation.Inequal.Operator;

@Table(name="Bid", schema="broker", uniqueConstraints = @UniqueConstraint(columnNames = {"bidderReference, auctionReference"}))
@Entity
@PrimaryKeyJoinColumn(name="bidIdentity")
@Inequal(leftAccessPath={"auction", "seller", "identity"}, rightAccessPath={"bidder", "identity"}, operator = Operator.NOT_EQUAL)
@Inequal(leftAccessPath="price", rightAccessPath={"auction", "askingPrice"}, operator=Operator.GREATER_EQUAL)
public class Bid extends BaseEntity{
	
	@XmlElement
	@Column(nullable=false, insertable=true, updatable=true)
	@Min(1)
	private long price;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="auctionReference", nullable=false, insertable=true, updatable=false)
	private Auction auction;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="bidderReference", nullable=false, insertable=true, updatable=false)
	private Person bidder;
	
	//default constructor
	protected Bid(){
		this(null, null);
	}
	
	public Bid(Auction auction, Person bidder){
		this.price = (auction == null) ? 1 : auction.getAskingPrice();
		this.auction = auction;
		this.bidder = bidder;
	}
	
	@XmlElement
	@XmlAuctionAsEntityFilter
	public Auction getAuction() {
		return auction;
	}
	
	@XmlElement
	@XmlAuctionAsReferenceFilter
	public long getAuctionReference() {
		return (this.auction == null) ? 0 : auction.getIdentity();
	}
	
	@XmlElement
	@XmlBidderAsEntityFilter
	public Person getBidder() {
		return bidder;
	}
	
	@XmlElement
	@XmlBidderAsReferenceFilter
	public long getBidderReference() {
		return (this.bidder == null) ? 0 : bidder.getIdentity();
	}
	
	public long getPrice() {
		return price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
	
	/**
	 * Filter annotation for associated bidders marshaled as entities.
	 */
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@EntityFiltering
	@SuppressWarnings("all")
	static public @interface XmlBidderAsEntityFilter {
		static final class Literal extends AnnotationLiteral<XmlBidderAsEntityFilter> implements XmlBidderAsEntityFilter {}
	}

	/**
	 * Filter annotation for associated bidders marshaled as references.
	 */
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@EntityFiltering
	@SuppressWarnings("all")
	static public @interface XmlBidderAsReferenceFilter {
		static final class Literal extends AnnotationLiteral<XmlBidderAsReferenceFilter> implements XmlBidderAsReferenceFilter {};
	}

	/**
	 * Filter annotation for associated auctions marshaled as entities.
	 */
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@EntityFiltering
	@SuppressWarnings("all")
	static public @interface XmlAuctionAsEntityFilter {
		static final class Literal extends AnnotationLiteral<XmlAuctionAsEntityFilter> implements XmlAuctionAsEntityFilter {}
	}

	/**
	 * Filter annotation for associated auctions marshaled as references.
	 */
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@EntityFiltering
	@SuppressWarnings("all")
	static public @interface XmlAuctionAsReferenceFilter {
		static final class Literal extends AnnotationLiteral<XmlAuctionAsReferenceFilter> implements XmlAuctionAsReferenceFilter {}
	}
}