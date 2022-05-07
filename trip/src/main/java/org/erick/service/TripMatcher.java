package org.erick.service;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class TripMatcher {
	
	@Scheduled(every="10s")
	public void match() {
		System.out.println("Criando corridas..");
		
	}

}
