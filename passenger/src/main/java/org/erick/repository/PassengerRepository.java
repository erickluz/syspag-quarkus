package org.erick.repository;

import javax.enterprise.context.ApplicationScoped;

import org.erick.domain.Passenger;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PassengerRepository implements PanacheRepository<Passenger> {

    public Passenger findByName(String name) {
        return find("name", name).firstResult();
    }

}
