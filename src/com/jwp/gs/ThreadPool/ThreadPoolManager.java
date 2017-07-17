package com.jwp.gs.ThreadPool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jwp.gs.manager.AbstractManager;

public class ThreadPoolManager extends AbstractManager {
	
	public static final int CORE_POOL_SIZE = 10;
	public static final int MAX_POOL_SIZE = 300;
	protected ThreadPoolExecutor executor;
	private LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<Runnable>();
	
	public ThreadPoolManager(){
		executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 30L, TimeUnit.SECONDS, queue);
		executor.allowCoreThreadTimeOut(true);
	}
	
	public void execute(Runnable call) {
		executor.execute(call);
	}
	
	public int getSize(){
		return queue.size();
	}
	
	public void setCoreSize(int size){
		((ThreadPoolExecutor)executor).setCorePoolSize(size);
	}
	
	public int getCoreSize(){
		return ((ThreadPoolExecutor)executor).getCorePoolSize();
	}
	
	public int getMaxSize(){
		return ((ThreadPoolExecutor)executor).getMaximumPoolSize();
	}

}
