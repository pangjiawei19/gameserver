package com.jwp.gs.service;

import java.util.Set;

/**
 * 业务接口
 * @author pangjiawei
 * @date 2017年7月12日 下午7:54:16
 */
public interface Service extends Iterable<Service> {

	/**
	 * 启动
	 * @throws Exception
	 */
	public void startup() throws Exception;
	/**
	 * 关闭
	 */
	public void shutdown();
	/**
	 * 重新加载
	 * @throws Exception
	 */
	public void reload() throws Exception;

	/**
	 * 返回前置Service集合
	 * 即必须初始化所有前置Service以后才能初始化该Service
	 * @return 如果没有约束返回null即可
	 */
	public Set<Class<? extends Service>> preInitServices();
}
