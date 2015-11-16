package de.sb.broker.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;
import java.util.Set;

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

import de.sb.broker.model.Auction;
import de.sb.broker.model.Bid;
import de.sb.broker.model.Person;

@Path("/people")
public class PersonService {

	final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("broker");
	final static EntityManager em = emf.createEntityManager();
	
	@GET
	@Produces("application/json")
	public List<Person> getPeople(
			@QueryParam("resultOffset") int resultOffset,
			@QueryParam("resultLength") int resultLength) {
		
		TypedQuery<Person> query = em.createQuery("select p from Person as p", Person.class);
		List<Person> allPeople= query.getResultList();
		return allPeople.subList(resultOffset, (resultLength == 0) ?  allPeople.size() : resultLength);
	}
	
	@PUT
	public void createOrUpdatePerson() {
		//TODO
	}
	
	@GET
	@Path("{identity}")
	@Produces("application/json")
	public Person getPerson(@PathParam("identity") long identity) {
		try {
			return em.find(Person.class, identity);
		} catch (final EntityNotFoundException exception) {
			throw new ClientErrorException(NOT_FOUND);
		}
	}
	
	@GET
	@Path("/{identity}/auctions")
	public Set<Auction> getAuction(
			@PathParam("identity") long identity,
			@QueryParam("ResultOffset") int ResultOffset,
			@QueryParam("ResultLength") int ResultLength) {
		try {
			Person person = em.find(Person.class, identity);
			return person.getAuctions();
		} catch (final EntityNotFoundException exception) {
			throw new ClientErrorException(NOT_FOUND);
		}
		
	}
	
	@GET
	@Path("/{identity}/bids")
	public Set<Bid> getBids(
			@PathParam("identity") long identity,
			@QueryParam("ResultOffset") int ResultOffset,
			@QueryParam("ResultLength") int ResultLength) {
		try {
			Person person = em.find(Person.class, identity);
			return person.getBids();
		} catch (final EntityNotFoundException exception) {
			throw new ClientErrorException(NOT_FOUND);
		}
	}
}
