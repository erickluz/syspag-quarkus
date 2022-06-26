package org.erick.service;

import java.time.LocalDateTime;

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

@ApplicationScoped
public class TripService {

	@Inject
	private CacheService cache;
	@Inject
	private TravelRepository travelRepository;
	@Inject
	private TravelStatusHistoryRepository travelStatusHistoryRepository;


	@Transactional
	public void passengerRequestsTrip(TripRequestPassenger tripRequest) {
		PassengerCache passengerCache = new PassengerCache(tripRequest.getIdPassenger(),tripRequest.getAddressOrigin(), tripRequest.getAddressDestiny(), tripRequest.getDistrict());
		DriverCache driverCache = cache.getDriverAvailaibleForPassenger(passengerCache.getAddressDestiny(), passengerCache.getDistrict());
		if (driverCache != null) {
			System.out.println("Motorista encontrado" + driverCache);
			startTrip(passengerCache, driverCache);
		} else {
			System.out.println("Motorista nao encontrado");
			cache.putPassegerOnHold(tripRequest.getIdPassenger(), passengerCache);
		}
	}

	private void startTrip(PassengerCache passengerCache, DriverCache driverCache) {
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

	public void driverSignalsAvailability(DriverAvailability driverAvailability) {
		DriverCache driverCache = new DriverCache(driverAvailability.getIdDriver(), driverAvailability.getCurrentAddress(), driverAvailability.getDistrict(), null);
		System.out.println(driverCache.toString());
		cache.signalDriverAvailability(driverAvailability.getIdDriver(), driverCache);
	}

}
