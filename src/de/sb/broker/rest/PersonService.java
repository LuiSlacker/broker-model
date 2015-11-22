package de.sb.broker.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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

import de.sb.broker.model.Auction;
import de.sb.broker.model.Bid;
import de.sb.broker.model.Person;

@Path("/people")
public class PersonService {

	final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("broker");
	final static EntityManager em = emf.createEntityManager();
	
	@GET
	@Produces({"application/xml", "application/json"})
	public Collection<Person> getPeople(
			@QueryParam("resultOffset") int resultOffset,
			@QueryParam("resultLength") int resultLength,
			@QueryParam("alias") String alias,
			@QueryParam("firstName") String firstName,
			@QueryParam("familyName") String familyName,
			@QueryParam("email") String email,
			@QueryParam("phone") String phone,
			@QueryParam("street") String street,
			@QueryParam("postcode") String postcode,
			@QueryParam("city") String city) {
		
		TypedQuery<Long> query = em.createQuery("select p.identity from Person as p WHERE"
				+ "(:alias is null or p.alias = :alias) and"
				+ "(:firstName is null or p.name.given = :firstName) and"
				+ "(:familyName is null or p.name.family = :familyName) and"
				+ "(:email is null or p.contact.email = :email) and"
				+ "(:phone is null or p.contact.phone = :phone) and"
				+ "(:street is null or p.address.street = :street) and"
				+ "(:postcode is null or p.address.postcode = :postcode) and"
				+ "(:city is null or p.address.city = :city)", Long.class);
		query.setParameter("alias", alias);
		query.setParameter("firstName", firstName);
		query.setParameter("familyName", familyName);
		query.setParameter("email", email);
		query.setParameter("phone", phone);
		query.setParameter("street", street);
		query.setParameter("postcode", postcode);
		query.setParameter("city", city);
		
		if (resultOffset != 0) query.setFirstResult(resultOffset);
		if (resultLength != 0) query.setMaxResults(resultLength);
		
		Collection<Person> allPeople = new TreeSet<Person>(Comparator.comparing(Person::getAlias));
		List<Long> allPeopleIds = query.getResultList();
		for (long personId : allPeopleIds) {
			try {
				allPeople.add(em.find(Person.class, personId));
			} catch (EntityNotFoundException e) {
				throw new ClientErrorException(NOT_FOUND);
			}
		}
		return allPeople;
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
