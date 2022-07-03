package org.erick.service;

import javax.inject.Inject;

import io.quarkus.scheduler.Scheduled;

public class TripMatcher {

	@Inject
	private TripService tripService;
    
	@Scheduled(every="5s")
	public void match() {
		tripService.matchTrip();
	}

}