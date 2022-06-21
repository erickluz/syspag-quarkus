package org.erick.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.erick.domain.Travel;
import org.erick.service.TripService;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@Path("/trip")
public class TripResource implements PanacheRepository<Travel> {
	
	@Inject
	private TripService tripService;
	
	@POST
	@Path("/driver/{id}")
	public Response driverSignalsAvailability(@PathParam("id") Long idDriver) {
		tripService.driverSignalsAvailability(idDriver);
		return Response.noContent().build();
	}
	
	@GET
	@Path("/passenger/waiting")
	public Response passengerWaiting() {
		List<String> passengers = tripService.getAllPassengersWaiting();	
		return Response.ok(passengers).build();
	}
	
	@GET
	@Path("/driver/waiting")
	public Response driversWaiting() {
		List<String> drivers = tripService.getAllDriversWaiting();	
		return Response.ok(drivers).build();
	}

}