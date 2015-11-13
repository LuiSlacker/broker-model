package de.sb.broker.rest;

import static de.sb.broker.model.Person.Group.ADMIN;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.websocket.server.PathParam;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import de.sb.broker.model.BaseEntity;
import de.sb.broker.model.Person;

public class PersonService {

	@GET
	@Path("people")
	public List getPeople(@QueryParam("ResultOffset") int ResultOffset,
							@QueryParam("ResultLength") int ResultLength) {
		
		final EntityManager brokerManager = LifeCycleProviderSkeleton.brokerManager();
		
		TypedQuery<Person> query = brokerManager.createQuery("select p from Person as p", Person.class);
		List<Person> personlist = query.getResultList();
		return personlist;
	}
	
	@PUT
	@Path("people")
	public void createOrUpdatePerson() {
		
	}
	
	@GET
	@Path("people/{identity}")
	public void getPersonIdentity(@PathParam("identity") long identity) {
		final EntityManager brokerManager = LifeCycleProviderSkeleton.brokerManager();

		brokerManager.getEntityManagerFactory().getCache().evict(Person.class, identity);
		try {
			final Person entity = brokerManager.getReference(Person.class, identity);
		} catch (final EntityNotFoundException exception) {
			throw new ClientErrorException(NOT_FOUND);
		}
	}
	
	@GET
	@Path("people/{identity}/auctions")
	public void getAuction(@PathParam("identity") long identity,
							@QueryParam("ResultOffset") int ResultOffset,
							@QueryParam("ResultLength") int ResultLength) {
						
	}
	
	@GET
	@Path("people/{identity}/bids")
	public void getBids(@PathParam("identity") long identity,
							@QueryParam("ResultOffset") int ResultOffset,
							@QueryParam("ResultLength") int ResultLength) {
		
	}
}
