package com.jwp.gs.update;

import com.jwp.gs.manager.Manager;

/**
 * 更新逻辑接口
 * @author pangjiawei
 * @date 2017年7月14日 下午5:07:04
 */
public interface Updateable extends Manager {

	/**
	 * 更新逻辑
	 * @param workerId 工作线程id，>0表示工作线程，0表示主线程
	 */
	public void update(int workerId);
	
	
	/**
	 * 是否主线程处理，true表示主线程处理，false表示工作线程处理
	 * @return
	 */
	public boolean isMainWorkerDeal();
	
	/**
	 * 是否工作线程处理
	 * @return
	 */
	public boolean isWorkerDeal();
	
}
