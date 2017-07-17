package com.jwp.gs.worker;

import com.jwp.gs.Platform;
import com.jwp.gs.update.UpdateManager;

/**
 * 核心线程（主线程）
 * @author pangjiawei
 * @date 2017年7月14日 下午4:27:58
 */
public class MainWorker extends Worker {

	public MainWorker() {
		super(0);
	}
	
	public UpdateManager getUpdateManager() {
		return (UpdateManager) Platform.get(UpdateManager.class, UpdateManager.TYPE_MAIN);
	}

}
