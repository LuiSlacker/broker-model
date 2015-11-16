package de.sb.broker.rest;

import java.util.List;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;

import de.sb.broker.model.Auction;

@Path("/auctions")
public class AuctionService {

	final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("broker");
	final static EntityManager em = emf.createEntityManager();
	
	@GET
	public List<Auction> getAuctions(
			@QueryParam("resultOffset") int resultOffset,
			@QueryParam("resultLength") int resultLength) {
		
		TypedQuery<Auction> query = em.createQuery("SELECT a FROM Auction a", Auction.class);
		List<Auction> allAuctions = query.getResultList();
		return allAuctions.subList(resultOffset, (resultLength==0) ? allAuctions.size() : resultLength);
	}
	
	@PUT
	public void createOrUpdateAuctions(@PathParam("identity") long identity) {
		//TODO
	}
	
	@GET
	@Path("/{identity}")
	public Auction getAuctionIdentity(@PathParam("identity") long identity) {
		try {
			return em.getReference(Auction.class, identity);
		} catch (EntityNotFoundException e) {
			throw new ClientErrorException(Status.NOT_FOUND);
		}
	}
}
