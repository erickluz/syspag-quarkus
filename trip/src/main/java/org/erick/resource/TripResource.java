package org.erick.resource;

import javax.ws.rs.Path;

import org.erick.domain.Travel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@Path("/trip")
public class TripResource implements PanacheRepository<Travel> {

}