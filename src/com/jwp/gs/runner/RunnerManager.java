package com.jwp.gs.runner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jwp.gs.manager.AbstractManager;
import com.jwp.gs.update.Updateable;

public class RunnerManager extends AbstractManager implements Updateable {
	
	private Map<String, Runner> threads = new ConcurrentHashMap<String, Runner>();

	/**
	 * 运行并监控线程
	 * @param r Runnable实例
	 * @param name 线程名
	 * @param isDaemon 是否为守护线程
	 */
	public void addRunner(Runnable r, String name, boolean isDaemon){
		Runner re = new Runner(name, isDaemon, r);
		threads.put(re.getName(), re);
		Thread t = re.buildAndBindThread();
		t.start();
		System.out.println("Runner add & start :" + name + ", " + t.getState());
	}
	
	@Override
	public void update(int workerId) {
		for(String name : threads.keySet()){
			Runner re = threads.get(name);
			Thread original = re.getT();
			if(original == null || !original.isAlive()){
				Thread t = re.buildAndBindThread();
				t.start();
				System.out.println("Runner restart :" + name  + "," + t.getState() + "," + (original == null ? null : original.getState()));
			}
		}
	}

	@Override
	public boolean isMainWorkerDeal() {
		return true;
	}

	@Override
	public boolean isWorkerDeal() {
		return false;
	}
	
}
