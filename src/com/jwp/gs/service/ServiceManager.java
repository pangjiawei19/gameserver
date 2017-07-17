package com.jwp.gs.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jwp.gs.manager.AbstractManager;

public class ServiceManager extends AbstractManager {

	private Map<Class<? extends Service>, Service> services = new LinkedHashMap<>();

	public void add(Service service) throws Exception {
		service.startup();
		services.put(service.getClass(), service);
		System.out.println("Service init : " + service.getClass().getSimpleName());
	}

	public Service get(Class<? extends Service> clazz) {
		return services.get(clazz);
	}

	public Collection<Service> getAllService() {
		return services.values();
	}
}
