package com.jwp.gs.worker;

import com.jwp.gs.Platform;
import com.jwp.gs.ThreadPool.ThreadPoolManager;
import com.jwp.gs.service.Service;
import com.jwp.gs.service.ServiceManager;

public class ShutdownHook implements Runnable {

	@Override
	public void run() {
		try {
			Worker.running = false;
			while (true) {
				System.out.println("shutdown hook cycle start");
				ThreadPoolManager tpm = (ThreadPoolManager) Platform.get(ThreadPoolManager.class);
				if (tpm.getSize() < 1) {
					System.out.println("start server shutdown!!");
					ServiceManager manager = (ServiceManager) Platform.get(ServiceManager.class);

					for (Service service : manager.getAllService()) {
						try {
							service.shutdown();
							System.out.println(service.getClass() + ":shutdown ok");
						} catch (Throwable e) {
							System.out.println(service.getClass() + ":shutdown fail");
						}
					}

					System.out.println("Server shutdown ok");
					break;
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
