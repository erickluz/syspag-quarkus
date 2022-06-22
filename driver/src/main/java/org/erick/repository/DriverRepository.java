package org.erick.repository;

import javax.enterprise.context.ApplicationScoped;

import org.erick.domain.Driver;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class DriverRepository implements PanacheRepository<Driver> {
    
}
