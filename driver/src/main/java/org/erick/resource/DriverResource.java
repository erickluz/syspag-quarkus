package org.erick.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.erick.domain.Driver;
import org.erick.domain.DriverAvailability;
import org.erick.service.DriverService;

@Path("/driver")
public class DriverResource {
    
    @Inject
    private DriverService driverService;

    @POST
    public Response saveDriver(Driver driver) {
        driverService.save(driver);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response findPassengerById(@PathParam("id") Long id) {
        Driver driver = driverService.findById(id);
        return Response.ok(driver).build();
    }

    @POST
    @Path("/trip")
    public Response signalAvailability(DriverAvailability driverAvailability) {
        driverService.signalAvailability(driverAvailability);
        return Response.noContent().build();
    }

}