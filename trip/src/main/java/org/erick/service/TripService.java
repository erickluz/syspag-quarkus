package org.erick.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TripService {

	@Inject
	private CacheService cache;
	
	public void passengerRequestsTrip(Long idPassenger) {
		cache.setPasseger(idPassenger, "testePassenger");
	}

	public void driverSignalsAvailability(Long idDriver) {
		cache.setDriver(idDriver, "testeDriver");
	}
	
	public List<String> getAllPassengersWaiting() {
		return cache.getAllPassengersWaiting();
	}
	
	public List<String> getAllDriversWaiting() {
		return cache.getAllDriversWaiting();
	}

}
