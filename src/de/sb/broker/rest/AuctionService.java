package de.sb.broker.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;

import de.sb.broker.model.Auction;

@Path("/auctions")
public class AuctionService {

	final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("broker");
	final static EntityManager em = emf.createEntityManager();
	
	@GET
	@Produces({"application/xml", "application/json"})
	public Collection<Auction> getAuctions(
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
		
		TypedQuery<Long> query = em.createQuery("SELECT a.identity FROM Auction a WHERE "
				+ "(:title is null or a.title = :title) and"
				+ "(:UCLower is null or a.unitCount >= :UCLower) and"
				+ "(:UCUpper is null or a.unitCount <= :UCUpper) and"
				+ "(:priceLower is null or a.askingPrice >= :priceLower) and"
				+ "(:priceUpper is null or a.askingPrice <= :priceUpper) and"
				+ "(:creationtimeLower is null or a.creationTimestamp >= :creationtimeLower) and"
				+ "(:creationtimeUpper is null or a.creationTimestamp <= :creationtimeUpper) and"
				+ "(:closuretimeLower is null or a.closureTimestamp <= :closuretimeLower) and"
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
		
		if (resultOffset != 0) query.setFirstResult(resultOffset);
		if (resultLength != 0) query.setMaxResults(resultLength);
		
		Collection<Auction> allAuctions = new TreeSet<Auction>(Comparator.comparing(Auction::getTitle));
		List<Long> allAuctionIds = query.getResultList();
		for (Long auctionId : allAuctionIds) {
			try {
				allAuctions.add(em.find(Auction.class, auctionId));
			} catch (EntityNotFoundException e) {
				throw new ClientErrorException(NOT_FOUND);
			}
		}
		return allAuctions;
	}
	
	@PUT
	public void createOrUpdateAuctions(@PathParam("identity") long identity) {
		//TODO
	}
	
	@GET
	@Path("/{identity}")
	@Produces({"application/xml", "application/json"})
	public Auction getAuctionIdentity(@PathParam("identity") long identity) {
		try {
			return em.find(Auction.class, identity);
		} catch (EntityNotFoundException e) {
			throw new ClientErrorException(Status.NOT_FOUND);
		}
	}
}