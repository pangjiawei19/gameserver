package com.jwp.gs;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jwp.gs.manager.Manager;
import com.jwp.gs.update.UpdateManager;


public class Platform {
	
	public static Map<String, Manager> managers = new LinkedHashMap<>();
	
	public static void add(Manager manager) throws Exception{
		managers.put(manager.getName(), manager);
		System.out.println("Manager init : " + manager.getName());
	}
	
	public static Manager get(Class<? extends Manager> clazz, Object...obj){
		String name = clazz.getSimpleName();
		if(clazz.equals(UpdateManager.class)) {
			if(obj.length < 1 || !(obj[0] instanceof Integer)) {
				return null;
			}
			int type = (int) obj[0];
			name = UpdateManager.genManagerName(type);
		}
		return managers.get(name);
	}
}
