package org.erick.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

@ApplicationScoped
public class CacheService {

    private RemoteCache<Long, String> passengerCache;
    private RemoteCache<Long, String> driverCache;
	
    @PostConstruct
    public void init() {
    	System.out.println("Instanciando cache...");
    	this.passengerCache = passengerCache(remoteCacheManager(), new CacheListener());
    	this.driverCache = driverCache(remoteCacheManager(), new CacheListener());
    	System.out.println("Cache Instaciado...");
    }
    
    public RemoteCache<Long, String> passengerCache(
    		RemoteCacheManager cacheManager, 
    		CacheListener listener) {
    	return cacheManager.getCache("passenger");
    }
    
    public RemoteCache<Long, String> driverCache(
    		RemoteCacheManager cacheManager, 
    		CacheListener listener) {
    	return cacheManager.getCache("driver");
    }

	public String findPassenger(Long idPassenger) {
	    return passengerCache.get(idPassenger);
	}
	
	public String findDriver(Long idDriver) {
	    return driverCache.get(idDriver);
	}
	
	public void setPasseger(Long idPassenger, String regiao) {
		passengerCache.put(idPassenger, regiao);
	}
	
	public void setDriver(Long idDriver, String regiao) {
		driverCache.put(idDriver, regiao);
	}

	public DefaultCacheManager defaultCacheManager() {
	    return new DefaultCacheManager();
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

	public List<String> getAllPassengersWaiting() {
		return new ArrayList<>(passengerCache.values());
	}

	public List<String> getAllDriversWaiting() {
		return new ArrayList<>(driverCache.values());
	}
}
