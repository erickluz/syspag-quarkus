package org.erick.service;

import io.quarkus.scheduler.Scheduled;

public class TripMatcher {
    
	@Scheduled(every="10s")
	public void match() {
		System.out.println("Matching trips..");
		
	}

}