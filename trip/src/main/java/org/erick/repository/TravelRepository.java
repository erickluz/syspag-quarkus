package org.erick.repository;

import javax.enterprise.context.ApplicationScoped;

import org.erick.domain.travel.Travel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TravelRepository implements PanacheRepository<Travel> {

}