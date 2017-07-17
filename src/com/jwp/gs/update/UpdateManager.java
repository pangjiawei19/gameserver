package com.jwp.gs.update;

import java.util.ArrayList;
import java.util.List;

import com.jwp.gs.Platform;
import com.jwp.gs.manager.AbstractManager;
import com.jwp.gs.util.DateUtil;

public class UpdateManager extends AbstractManager {
	
	public static final int TYPE_MAIN = 1;
	public static final int TYPE_WORKER = 2;
	

	private List<Updateable> syncUpdatables = new ArrayList<Updateable>();
	private int type;
	
	public UpdateManager(int type) {
		this.type = type;
	}

	public void addSyncUpdatable(Updateable updatable) {
		syncUpdatables.add(updatable);
		System.out.println(this.getName() + " init : " + updatable.getName());
	}

	public boolean update(int workerId) {
		for (Updateable up : syncUpdatables) {
			try {
				long old = DateUtil.getCurrentTimeMilis();
				up.update(workerId);
				long dis = DateUtil.getCurrentTimeMilis() - old;
				if (dis > 40) {
					System.out.println("TooLong,Update," + workerId + "," + dis + "," + up.getClass().getSimpleName());
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return genManagerName(type);
	}
	
	public static String genManagerName(int type) {
		return UpdateManager.class.getSimpleName() + "#" + type;
	}
	
	public static UpdateManager getUpdateManager(int type) {
		return (UpdateManager) Platform.get(UpdateManager.class, type);
	}

}
