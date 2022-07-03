package org.erick.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.erick.domain.driver.DriverAvailability;
import org.erick.domain.driver.DriverCache;
import org.erick.domain.passenger.PassengerCache;
import org.erick.domain.passenger.TripRequestPassenger;
import org.erick.domain.travel.StatusTravel;
import org.erick.domain.travel.Travel;
import org.erick.domain.travel.TravelStatusHistory;
import org.erick.repository.TravelRepository;
import org.erick.repository.TravelStatusHistoryRepository;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TripService {
	
	private static final Logger LOG = Logger.getLogger(TripService.class);
	@Inject
	private CacheService cache;
	@Inject
	private TravelRepository travelRepository;
	@Inject
	private TravelStatusHistoryRepository travelStatusHistoryRepository;

	public void passengerRequestsTrip(TripRequestPassenger tripRequest) {
		PassengerCache passengerCache = new PassengerCache(tripRequest.getIdPassenger(),tripRequest.getAddressOrigin(), tripRequest.getAddressDestiny(), tripRequest.getDistrict());
		DriverCache driverCache = cache.getDriverAvailaibleForPassenger(passengerCache.getAddressDestiny(), passengerCache.getDistrict());
		if (driverCache != null) {
			startTrip(passengerCache, driverCache);
		} else {
			cache.putPassegerOnHold(tripRequest.getIdPassenger(), passengerCache);
		}
	}
	
	public void driverSignalsAvailability(DriverAvailability driverAvailability) {
		DriverCache driverCache = new DriverCache(driverAvailability.getIdDriver(), driverAvailability.getCurrentAddress(), driverAvailability.getDistrict(), null);
		LOG.debug(driverCache.toString());
		cache.signalDriverAvailability(driverAvailability.getIdDriver(), driverCache);
	}

	@Transactional
	public void startTrip(PassengerCache passengerCache, DriverCache driverCache) {
			LOG.debug("Trip started. Passenger ID: " + passengerCache.getId() + " Driver ID: " + driverCache.getId());
			Travel travel = new Travel();
			travel.setIdDriver(driverCache.getId());
			travel.setIdPassenger(passengerCache.getId());
			travelRepository.persist(travel);
			TravelStatusHistory travelStatusHistory = new TravelStatusHistory();
			travelStatusHistory.setStatus(StatusTravel.STATED);
			travelStatusHistory.setDatetimeStatus(LocalDateTime.now());
			travelStatusHistory.setTravel(travel);
			travelStatusHistoryRepository.persist(travelStatusHistory);
	}

	public void matchTrip() {
		LOG.debug("Matching trips...");
		List<PassengerCache> passengersWaiting = cache.getPassengersWaiting();
		LOG.debug("Passengers waiting for trip: " + passengersWaiting.size());
		passengersWaiting.stream().forEach(passenger -> {
			DriverCache driverCache = cache.getDriverAvailaibleForPassenger(passenger.getAddressDestiny(), passenger.getDistrict());
			if (driverCache != null) {
				startTrip(passenger, driverCache);
				cache.remotePassengerCache.remove(passenger.getId());
			}
		});
	}

}
