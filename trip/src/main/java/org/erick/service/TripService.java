package org.erick.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.erick.domain.DriverAvailability;
import org.erick.domain.DriverCache;
import org.erick.domain.PassengerCache;
import org.erick.domain.TripRequest;

@ApplicationScoped
public class TripService {

	@Inject
	private CacheService cache;
	
	public void passengerRequestsTrip(TripRequest tripRequest) {
		PassengerCache passengerCache = new PassengerCache(tripRequest.getAddressOrigin(), tripRequest.getAddressDestiny(), tripRequest.getDistrict());
		cache.setPasseger(tripRequest.getIdPassenger(), passengerCache);
	}

	public void driverSignalsAvailability(DriverAvailability driverAvailability) {
		DriverCache driverCache = new DriverCache(driverAvailability.getCurrentAddress(), driverAvailability.getDistrict(), null);
		cache.setDriver(driverAvailability.getIdDriver(), driverCache);
	}

}
