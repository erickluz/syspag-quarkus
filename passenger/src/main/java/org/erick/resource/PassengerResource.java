package org.erick.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.erick.domain.Passenger;
import org.erick.domain.TripRequest;
import org.erick.service.PassengerService;

@Path("/passenger")
public class PassengerResource {

    @Inject
    private PassengerService passengerService;

    @POST
    public Response savePassenger(Passenger passenger) {
        passengerService.save(passenger);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response findPassengerById(@PathParam("id") Long id) {
        Passenger passenger = passengerService.findById(id);
        return Response.ok(passenger).build();
    }

    @POST
    @Path("/trip")
    public Response requestTrip(TripRequest tripRequest) {
        passengerService.requestTrip(tripRequest);
        return Response.noContent().build();
    }

}