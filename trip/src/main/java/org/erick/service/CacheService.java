package org.erick.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transaction;

import org.erick.domain.driver.DriverCache;
import org.erick.domain.passenger.PassengerCache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import io.quarkus.infinispan.client.Remote;

@ApplicationScoped
public class CacheService {

	private static final Long CACHE_ORDER_DRIVER = 1L;

	RemoteCacheManager remoteCacheManager;

	@Inject
	@Remote("passenger")
    RemoteCache<Long, PassengerCache> remotePassengerCache;
	@Inject
	@Remote("driver")
    RemoteCache<Long, DriverCache> remoteDriverCache;
	@Inject
	@Remote("orderingDriver")
	RemoteCache<Long, Long> remoteOrderingDriver;
	
	@Inject CacheService(RemoteCacheManager remoteCacheManager) {
		this.remoteCacheManager = remoteCacheManager;
	 }

	public PassengerCache findPassenger(Long idPassenger) {
	    return remotePassengerCache.get(idPassenger);
	}
	
	public DriverCache findDriver(Long idDriver) {
	    return remoteDriverCache.get(idDriver);
	}

	public Long getCurrentOrderingDriver() {
		System.out.println("tamanho ordering:" + remoteOrderingDriver.size());
		if (remoteOrderingDriver.size() == 0) {
			remoteOrderingDriver.put(CACHE_ORDER_DRIVER, 1L);
		}
		return remoteOrderingDriver.get(CACHE_ORDER_DRIVER);
	}
	
	public void putPassegerOnHold(Long idPassenger, PassengerCache passengerCache) {
		this.remotePassengerCache.put(idPassenger, passengerCache);
	}
	
	public void signalDriverAvailability(Long idDriver, DriverCache driverCache) {
		Long currentOrdering = getCurrentOrderingDriver();
		driverCache.setOrdering(++currentOrdering);
		this.remoteOrderingDriver.put(CACHE_ORDER_DRIVER, currentOrdering);
		this.remoteDriverCache.put(idDriver, driverCache);
	}

	public DriverCache getDriverAvailaibleForPassenger(String address, String district) {
		QueryFactory queryFactory = org.infinispan.client.hotrod.Search.getQueryFactory(remoteDriverCache);
		Query<DriverCache> query = queryFactory.create(
		"	FROM org.erick.domain.DriverCache " +
		"	WHERE ((address = :address) OR (district = :district))" +
		"	ORDER BY ordering ");
		query.setParameter("address", address);
		query.setParameter("district", district);
		List<DriverCache> drivers = query.execute().list();
		System.out.println("query: " + drivers.toString());
		DriverCache driver = drivers.stream().findFirst().orElse(null);
		if (driver != null) {
			remoteDriverCache.remove(driver.getId());
		}
		return driver;
	}
	
	public PassengerCache getPassengerCache() {
		QueryFactory qf = org.infinispan.client.hotrod.Search.getQueryFactory(remotePassengerCache);
		Query<Transaction> query = qf.create("from org.erick.domain.PassengerCache where price > 20");
		return (PassengerCache) query.execute();
	}

}