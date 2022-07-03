package org.erick.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.erick.domain.driver.DriverCache;
import org.erick.domain.passenger.PassengerCache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.jboss.logging.Logger;

import io.quarkus.infinispan.client.Remote;

@ApplicationScoped
public class CacheService {

	private static final Long CACHE_ORDER_DRIVER = 1L;
	private static final Logger LOG = Logger.getLogger(CacheService.class);
	
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
		if (remoteOrderingDriver.size() == 0) {
			remoteOrderingDriver.put(CACHE_ORDER_DRIVER, 1L);
		}
		return remoteOrderingDriver.get(CACHE_ORDER_DRIVER);
	}
	
	public void putPassegerOnHold(Long idPassenger, PassengerCache passengerCache) {
		LOG.debug("Passenger was put on hold. " + passengerCache);
		this.remotePassengerCache.put(idPassenger, passengerCache);
	}
	
	public void signalDriverAvailability(Long idDriver, DriverCache driverCache) {
		LOG.debug("Driver signal availability. " + idDriver);
		Long currentOrdering = getCurrentOrderingDriver();
		driverCache.setOrdering(++currentOrdering);
		this.remoteOrderingDriver.put(CACHE_ORDER_DRIVER, currentOrdering);
		this.remoteDriverCache.put(idDriver, driverCache);
	}

	public DriverCache getDriverAvailaibleForPassenger(String address, String district) {
		LOG.debug("Searching driver for Address: " + address + " and District: " + district);
		List<DriverCache> drivers = getQueryDriverByAddresOrDistrict(address, district);
		LOG.debug("Drivers found: " + drivers.toString());
		return getDriver(drivers);
	}

	private List<DriverCache> getQueryDriverByAddresOrDistrict(String address, String district) {
		QueryFactory queryFactory = org.infinispan.client.hotrod.Search.getQueryFactory(remoteDriverCache);
		Query<DriverCache> query = queryFactory.create(
		"	FROM org.erick.domain.DriverCache " +
		"	WHERE ((address = :address) OR (district = :district))" +
		"	ORDER BY ordering ");
		query.setParameter("address", address);
		query.setParameter("district", district);
		return query.execute().list();
	}

	private DriverCache getDriver(List<DriverCache> drivers) {
		DriverCache driver = drivers.stream().findFirst().orElse(null);
		if (driver != null) {
			remoteDriverCache.remove(driver.getId());
		}
		return driver;
	}

	public List<PassengerCache> getPassengersWaiting() {
		QueryFactory queryFactory = org.infinispan.client.hotrod.Search.getQueryFactory(remotePassengerCache);
		Query<PassengerCache> query = queryFactory.create("from org.erick.domain.PassengerCache");
		List<PassengerCache> passengersCaches = query.execute().list();
		LOG.debug("Passengers found: " + passengersCaches.toString());
		return passengersCaches;
	}

}