package com.jwp.gs.config;

import java.util.concurrent.ConcurrentHashMap;

public class ConfigOption<T> {

	private static final ConcurrentHashMap<String, ConfigOption<?>> options = new ConcurrentHashMap<>();

	public static final ConfigOption<String> CONF_SERVER_IP = new ConfigOption<>("serverIp");
	public static final ConfigOption<Integer> CONF_SERVER_ID = new ConfigOption<>("serverId");
	public static final ConfigOption<Integer> CONF_SERVER_PORT = new ConfigOption<>("serverPort");
	public static final ConfigOption<Integer> CONF_WORKER_COUNT = new ConfigOption<>("workerCount");

	private String name;

	protected ConfigOption(String name) {
		this.name = name;
		options.put(name, this);
	}

	public static ConfigOption<?> valueOf(String name) {
		return options.get(name);
	}

}
