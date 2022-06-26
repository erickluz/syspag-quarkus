package org.erick.repository;

import javax.enterprise.context.ApplicationScoped;

import org.erick.domain.travel.TravelStatusHistory;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TravelStatusHistoryRepository implements PanacheRepository<TravelStatusHistory> {
    
}
