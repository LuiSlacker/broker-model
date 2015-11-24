package de.sb.broker.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.Collection;
import java.util.Comparator;
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
		Collection<Long> allPeopleIds = query.getResultList();
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
	public Long createOrUpdatePerson(Person template) {
		final boolean persist = template.getIdentity() == 0;
		final Person person;
		if(persist){
			person = new Person();
		} else{
			person = em.find(Person.class, template.getIdentity());
		}
		person.setAlias(template.getAlias());
		person.setGroup(template.getGroup());
		person.getName().setFamily(template.getName().getFamily());
		person.getName().setGiven(template.getName().getGiven());
		person.getAddress().setCity(template.getAddress().getCity());
		person.getAddress().setPostcode(template.getAddress().getPostcode());
		person.getAddress().setStreet(template.getAddress().getStreet());
		person.getContact().setEmail(template.getContact().getEmail());
		person.getContact().setPhone(template.getContact().getPhone());
		
		em.getTransaction().begin();
		if (persist) em.persist(person);
		em.getTransaction().commit();
		em.close();
		return person.getIdentity();
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
	public Collection<Auction> getAuction(
			@PathParam("identity") long identity,
			@QueryParam("ResultOffset") int ResultOffset,
			@QueryParam("ResultLength") int ResultLength) {
		Person person;
		try {
			person = em.find(Person.class, identity);
		} catch (final EntityNotFoundException exception) {
			throw new ClientErrorException(NOT_FOUND);
		}
		Collection<Auction> allAuctions = new TreeSet<Auction>(Comparator.comparing(Auction::getTitle));
		allAuctions.addAll(person.getAuctions());
		for (Bid bid : person.getBids()) {
			allAuctions.add(bid.getAuction());
		}
		return allAuctions;
		
	}
	
	@GET
	@Path("/{identity}/bids")
	public Collection<Bid> getBids(
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
