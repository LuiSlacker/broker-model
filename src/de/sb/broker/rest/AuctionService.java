package de.sb.broker.rest;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.sb.broker.model.Auction;
import de.sb.broker.model.Bid;
import de.sb.broker.model.Person;

@Path("/auctions")
public class AuctionService {

	@GET
	@Produces({"application/xml", "application/json"})
	public Collection<Auction> getAuctions(
			@HeaderParam("Authorization") final String authentication,
			@QueryParam("title") String title,
			@QueryParam("UCLower") Long UCLower,
			@QueryParam("UCUpper") Long UCUpper,
			@QueryParam("priceLower") Long priceLower,
			@QueryParam("priceUpper") Long priceUpper,
			@QueryParam("resultOffset") int resultOffset,
			@QueryParam("resultLength") int resultLength, 
			@QueryParam("creationtimeLower") Long creationtimeLower,
			@QueryParam("creationtimeUpper") Long creationtimeUpper,
			@QueryParam("closuretimeLower") Long closuretimeLower,
			@QueryParam("closuretimeUpper") Long closuretimeUpper,
			@QueryParam("descriptionFrag") String descriptionFrag) {
		
		final EntityManager brokerManager = LifeCycleProvider.brokerManager();
		LifeCycleProvider.authenticate(authentication);
		TypedQuery<Long> query = brokerManager.createQuery("SELECT a.identity FROM Auction a WHERE "
				+ "(:title is null or a.title = :title) and"
				+ "(:UCLower is null or a.unitCount >= :UCLower) and"
				+ "(:UCUpper is null or a.unitCount <= :UCUpper) and"
				+ "(:priceLower is null or a.askingPrice >= :priceLower) and"
				+ "(:priceUpper is null or a.askingPrice <= :priceUpper) and"
				+ "(:creationtimeLower is null or a.creationTimestamp >= :creationtimeLower) and"
				+ "(:creationtimeUpper is null or a.creationTimestamp <= :creationtimeUpper) and"
				+ "(:closuretimeLower is null or a.closureTimestamp >= :closuretimeLower) and"
				+ "(:closuretimeUpper is null or a.closureTimestamp <= :closuretimeUpper) and"
				+ "(:descriptionFrag is null or a.description like :likeExpression)", Long.class);
		query.setParameter("title", title);
		query.setParameter("UCLower", UCLower);
		query.setParameter("UCUpper", UCUpper);
		query.setParameter("priceLower", priceLower);
		query.setParameter("priceUpper", priceUpper);
		query.setParameter("creationtimeLower", creationtimeLower);
		query.setParameter("creationtimeUpper", creationtimeUpper);
		query.setParameter("closuretimeLower", closuretimeLower);
		query.setParameter("closuretimeUpper", closuretimeUpper);
		query.setParameter("descriptionFrag", descriptionFrag);
		query.setParameter("likeExpression", "%" + descriptionFrag + "%");
		
		if (resultOffset > 0) query.setFirstResult(resultOffset);
		if (resultLength > 0) query.setMaxResults(resultLength);
		
		Collection<Auction> allAuctions = new TreeSet<Auction>(Comparator.comparing(Auction::getTitle));
		Collection<Long> allAuctionIds = query.getResultList();
		for (Long auctionId : allAuctionIds) {
			final Auction auction = brokerManager.find(Auction.class, auctionId);
			if (auction != null) {
				allAuctions.add(auction);
			} else {
				throw new NotFoundException();
			}
		}
		return allAuctions;
	}
	
	@PUT
	@Consumes({"application/xml", "application/json"})
	public Long createOrUpdateAuctions(
			Auction template,
			@HeaderParam("Authorization") final String authentication) {
		
		final EntityManager brokerManager = LifeCycleProvider.brokerManager();
		final Person requester = LifeCycleProvider.authenticate(authentication);
		final boolean persist = template.getIdentity() == 0;
		final Auction auction;
		if(persist){
			auction = new Auction(requester);
		} else {
			auction = brokerManager.find(Auction.class, template.getIdentity());
			if (auction == null) throw new NotFoundException();
			if (requester.getIdentity() != auction.getSellerReference()) throw new ForbiddenException();
			if (auction.isSealed()) throw new ForbiddenException();
		}
		
		auction.setTitle(template.getTitle());
		auction.setDescription(template.getDescription());
		auction.setClosureTimestamp(template.getClosureTimestamp());
		auction.setAskingPrice(template.getAskingPrice());
		auction.setUnitCount(template.getUnitCount());
		auction.setVersion(template.getVersion());
		
		try {
			if (persist) brokerManager.persist(auction);	
			else {
				brokerManager.flush();
			}
		} catch (ConstraintViolationException e) {
			throw new ClientErrorException(BAD_REQUEST);
		}
		try {
			brokerManager.getTransaction().commit();
		} catch (RollbackException e) {
			throw new ClientErrorException(CONFLICT);
		}
		finally {
			brokerManager.getTransaction().begin();
		}
		return auction.getIdentity();
	}
	
	@GET
	@Path("/{identity}")
	@Produces({"application/xml", "application/json"})
	public Auction getAuctionIdentity(
			@PathParam("identity") long identity,
			@HeaderParam("Authorization") final String authentication) {
		
		final EntityManager brokerManager = LifeCycleProvider.brokerManager();
		LifeCycleProvider.authenticate(authentication);
		final Auction auction =  brokerManager.find(Auction.class, identity);
		if (auction == null) throw new NotFoundException(); 
		return auction; 
	}
	
	@GET
	@Path("/{identity}/bid")
	@Produces({"application/xml", "application/json"})
	public Bid getBidForAuction(
			@PathParam("identity") long identity,
			@HeaderParam("Authorization") final String authentication) {
		
		final Person requester = LifeCycleProvider.authenticate(authentication);
		for (Bid bid : requester.getBids()) {
			if (bid.getAuction().getIdentity() == identity){
				return bid;
			}
		}
		return null;
	}
	
	@POST
	@Path("/{identity}/bid")
	@Consumes({"application/xml", "application/json"})
	public void CreateUpdateOrDeleteBid(
			@PathParam("identity") long identity,
			@HeaderParam("Authorization") final String authentication,
			@Valid @NotNull Bid template){
		
		final EntityManager brokerManager = LifeCycleProvider.brokerManager();
		final Person requester = LifeCycleProvider.authenticate(authentication);
		
		final boolean persist = template.getIdentity() == 0;
		
		Auction auction = brokerManager.find(Auction.class, identity);
		if (auction == null) throw new NotFoundException();
				
		final Bid bid;
		if(persist){
			bid = new Bid(auction, requester);
		} else  {
			bid = brokerManager.find(Bid.class, template.getIdentity());
			if (bid == null) throw new NotFoundException();
			if (bid.getBidderReference() != requester.getIdentity()) throw new ForbiddenException();
		}
			
		if (!persist && template.getPrice() == 0){
			brokerManager.remove(bid);
		} else{
			bid.setPrice(template.getPrice());
			bid.setVersion(template.getVersion());
		}

		try {
			if (persist) brokerManager.persist(bid);	
			else brokerManager.flush();
		} catch (ConstraintViolationException e) {
			throw new ClientErrorException(BAD_REQUEST);
		}

		try {
			brokerManager.getTransaction().commit();
		} catch (RollbackException e) {
			throw new ClientErrorException(CONFLICT);
		}
		finally {
			brokerManager.getTransaction().begin();
		}
	}
}
