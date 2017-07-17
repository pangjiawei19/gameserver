package com.jwp.gs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.reflections.Reflections;

import com.jwp.gs.config.ConfigOption;
import com.jwp.gs.config.Configuration;
import com.jwp.gs.manager.Manager;
import com.jwp.gs.runner.RunnerManager;
import com.jwp.gs.service.Service;
import com.jwp.gs.service.ServiceManager;
import com.jwp.gs.update.UpdateManager;
import com.jwp.gs.update.Updateable;
import com.jwp.gs.util.CommonUtil;
import com.jwp.gs.worker.MainWorker;
import com.jwp.gs.worker.ShutdownHook;
import com.jwp.gs.worker.Worker;

/**
 * 启动Server
 * @author pangjiawei
 * @date 2017年7月13日 下午7:30:39
 */
public abstract class Server {
	
	protected void init() {
		
		try {
			
			//初始化管理器
			this.initManager();
			//初始化更新器
			this.initUpdateable();
			//初始化Service
			this.initService();
			//自行初始化
			this.subInit();
			//挂载钩子函数
			Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
			
			//启动工作线程
			this.startWorker();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void initManager() throws Exception {
		//获得Manager所有实现类
		Reflections reflections = new Reflections("");
		Set<Class<? extends Manager>> subTypes = reflections.getSubTypesOf(Manager.class);
		
		for (Class<? extends Manager> c : subTypes) {
			if(Modifier.isAbstract(c.getModifiers()) || Modifier.isInterface(c.getModifiers())) {//过滤抽象类和接口
				continue;
			}
			if(c.equals(UpdateManager.class)) {
				Constructor<? extends Manager> con = c.getConstructor(int.class);
				Platform.add(con.newInstance(UpdateManager.TYPE_MAIN));
				Platform.add(con.newInstance(UpdateManager.TYPE_WORKER));
			}else {
				Platform.add(c.newInstance());//每个类通过反射生成对象
			}
		}
	}
	
	/**
	 * 初始化更新器
	 */
	private void initUpdateable() {
		//获得Updateable所有实现类
		Reflections reflections = new Reflections("");
		Set<Class<? extends Updateable>> subTypes = reflections.getSubTypesOf(Updateable.class);
		
		for (Class<? extends Updateable> c : subTypes) {
			if(Modifier.isAbstract(c.getModifiers()) || Modifier.isInterface(c.getModifiers())) {//过滤抽象类和接口
				continue;
			}
			
			Manager m = Platform.get(c);
			if(m == null) {
				throw new RuntimeException("initUpdateable error : can't find Updateable from Platform ," + c.getSimpleName());
			}
			
			Updateable u = (Updateable) m;
			if(u.isMainWorkerDeal()) {
				UpdateManager.getUpdateManager(UpdateManager.TYPE_MAIN).addSyncUpdatable(u);
			}
			if(u.isWorkerDeal()) {
				UpdateManager.getUpdateManager(UpdateManager.TYPE_WORKER).addSyncUpdatable(u);
			}
		}
	}
	
	/**
	 * Service初始化
	 * @throws Exception
	 */
	private void initService() throws Exception {
		
		//获得Service所有实现类
		Reflections reflections = new Reflections("");
		Set<Class<? extends Service>> subTypes = reflections.getSubTypesOf(Service.class);
		
		
		Set<Service> set = new HashSet<>();
		LinkedList<Service> ll = new LinkedList<>();//存储待初始化Service有序集合
		for (Class<? extends Service> c : subTypes) {
			if(Modifier.isAbstract(c.getModifiers()) || Modifier.isInterface(c.getModifiers())) {//过滤抽象类和接口
				continue;
			}
			
			Service service = c.newInstance();//每个类通过反射生成对象
			if(service.preInitServices() != null && service.preInitServices().size() > 0) {
				set.add(service);//有前置初始化需求，先保留
			}else {
				ll.offerLast(service);//没有前置初始化需求，放入待初始化集合
			}
		}
		
		int count = subTypes.size() + 10;
		while(count-- > 0) {//循环指定次进行排序处理，理论上最多循环的次数即Service所有实现类的个数
			
			Iterator<Service> itx = set.iterator();
			waitLoop:
			while(itx.hasNext()) {
				//逐一判断每个Servie的前置初始化条件
				Service service = itx.next();
				Set<Class<? extends Service>> pres = service.preInitServices();
				int maxIndex = 0;
				for (Class<? extends Service> clazz : pres) {
					int index = CommonUtil.indexOfListByClass(ll, clazz);
					if(index < 0) {
						//前置Service没有确定顺序，暂不处理
						continue waitLoop;
					}else {
						maxIndex = Math.max(maxIndex, index);
					}
				}
				//前置Service均已确定顺序，该Service插入到最后一个前置Service的后面
				ll.add(maxIndex + 1, service);
				itx.remove();
			}
		}
		
		if(set.size() > 0) {//循环结束后仍有未确定顺序的Service，则认为Service前置顺序设置有误，抛出异常
			StringBuilder sb = new StringBuilder();
			for (Service service : set) {
				sb.append(service.getClass().getName()).append(",");
			}
			throw new RuntimeException("Service preInfo error : " + sb.toString());
		}
		
		//按顺序初始化所有Service
		for (Service service : ll) {
			ServiceManager sm = (ServiceManager) Platform.get(ServiceManager.class);
			sm.add(service);
		}
	}
	
	/**
	 * 启动工作线程
	 */
	private void startWorker() {
		RunnerManager rm = (RunnerManager) Platform.get(RunnerManager.class);
		// 启动主线程
		rm.addRunner(new MainWorker(), "MainWorker", false);
		int workerCount = Configuration.getConfig(ConfigOption.CONF_WORKER_COUNT, 1);
		workerCount = Math.max(1, workerCount);//至少开启一个工作线程
		for (int i = 1; i <= workerCount; i++) {
			// 启动工作线程
			rm.addRunner(new Worker(i), "Worker-" + i, false);
		}
	}
	
	/**
	 * 具体业务初始化逻辑
	 * @throws Exception
	 */
	protected abstract void subInit() throws Exception;
	
	public static void main(String[] args) {
		try {
			
			Configuration.loadConfig("config.xml");
			new Server() {
				
				@Override
				protected void subInit() throws Exception {
					
				}
			}.init();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
