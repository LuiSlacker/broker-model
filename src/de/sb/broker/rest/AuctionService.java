package de.sb.broker.rest;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public class AuctionService {

	@GET
	@Path("auctions")
	public void getAuctions(@QueryParam("ResultOffset") int ResultOffset,
							  @QueryParam("ResultLength") int ResultLength) {
		
	}
	
	@PUT
	@Path("auctions")
	public void createOrUpdateAuctions(@PathParam("identity") long identity) {
		
	}
	
	@GET
	@Path("auctions/{identity}")
	public void getAuctionIdentity(@PathParam("identity") long identity) {
		
	}
}
