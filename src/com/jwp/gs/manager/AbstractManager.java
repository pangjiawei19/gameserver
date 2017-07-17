package com.jwp.gs.manager;



/**
 * 管理器抽象类
 * @author pangjiawei
 * @date 2017年7月14日 下午5:38:45
 */
public abstract class AbstractManager implements Manager {

	
	/**
	 * 默认获得该类的类名，如果有多个同类Manager需要区分，请自行实现
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}


}
