package com.jwp.gs.worker;

import com.jwp.gs.Platform;
import com.jwp.gs.update.UpdateManager;
import com.jwp.gs.util.DateUtil;

/**
 * 工作线程
 * 
 * @author pangjiawei
 * @date 2017年7月14日 下午4:33:02
 */
public class Worker implements Runnable {

	public static final int ROUND_TIME = 100;

	public static boolean running = true;

	private int id;
	protected long lastTime;
	protected int tick = 0;

	public Worker(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		while (running) {
			try {
				lastTime = DateUtil.getCurrentTimeMilis();

				this.getUpdateManager().update(this.id);

				if (tick == Integer.MAX_VALUE) {
					tick = 0;
				} else {
					tick++;
				}

				long dis = DateUtil.getCurrentTimeMilis() - lastTime;
				if (dis > 80) {
					System.out.println("TooLong,Worker," + id + "," + dis);
				}
				if (dis < ROUND_TIME) {
					if (dis < 0) {
						dis = 0;
					}
					Thread.sleep(ROUND_TIME - dis);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public int getId() {
		return id;
	}
	
	public UpdateManager getUpdateManager() {
		return (UpdateManager) Platform.get(UpdateManager.class, UpdateManager.TYPE_WORKER);
	}
}
