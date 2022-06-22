package org.erick.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transaction;

import org.erick.domain.DriverCache;
import org.erick.domain.PassengerCache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

@ApplicationScoped
public class CacheService {

    private RemoteCache<Long, PassengerCache> passengerCache;
    private RemoteCache<Long, DriverCache> driverCache;
	
    @PostConstruct
    public void init() {
    	System.out.println("Instanciando cache...");
    	this.passengerCache = passengerCache(remoteCacheManager(), new CacheListener());
    	this.driverCache = driverCache(remoteCacheManager(), new CacheListener());
    	System.out.println("Cache Instaciado...");
    }
    
    public RemoteCache<Long, PassengerCache> passengerCache(
    		RemoteCacheManager cacheManager, 
    		CacheListener listener) {
    	return cacheManager.getCache("passenger");
    }
    
    public RemoteCache<Long, DriverCache> driverCache(
    		RemoteCacheManager cacheManager, 
    		CacheListener listener) {
    	return cacheManager.getCache("driver");
    }

	public PassengerCache findPassenger(Long idPassenger) {
	    return passengerCache.get(idPassenger);
	}
	
	public DriverCache findDriver(Long idDriver) {
	    return driverCache.get(idDriver);
	}
	
	public void setPasseger(Long idPassenger, PassengerCache passengerCache) {
		this.passengerCache.put(idPassenger, passengerCache);
	}
	
	public void setDriver(Long idDriver, DriverCache driverCache) {
		this.driverCache.put(idDriver, driverCache);
	}

	public DefaultCacheManager defaultCacheManager() {
	    return new DefaultCacheManager();
	}
	
	public DriverCache getDriverCache() {
		QueryFactory qf = org.infinispan.client.hotrod.Search.getQueryFactory(driverCache);
		Query<Transaction> q = qf.create("from org.erick.domain.DriverCache where price > 20");
		return (DriverCache) q.execute();
	}
	
	public PassengerCache getPassengerCache() {
		QueryFactory qf = org.infinispan.client.hotrod.Search.getQueryFactory(passengerCache);
		Query<Transaction> q = qf.create("from org.erick.domain.PassengerCache where price > 20");
		return (PassengerCache) q.execute();
	}
	
	public RemoteCacheManager remoteCacheManager() {
		ConfigurationBuilder builder = new ConfigurationBuilder(); 
        builder.addServer().host("localhost").port(11222)
        	.security()
        	.authentication()
        	.saslMechanism("SCRAM-SHA-1")
        	.username("admin")
        	.password("admin");
        
        return new RemoteCacheManager(builder.build());
	}

}
