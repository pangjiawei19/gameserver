package com.jwp.gs.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Configuration {

	private static final Map<ConfigOption<?>, Object> configs = new HashMap<>();

	public static <T> T getConfig(ConfigOption<T> option) {
		return getConfig(option, null);
	}
	
	public static <T> T getConfig(ConfigOption<T> option, T def) {
		if(configs.containsKey(option)) {
			return (T) configs.get(option);
		}
		return def;
	}

	public static void loadConfig(String fileName) {
		SAXReader reader = new SAXReader();
		String dir = System.getProperty("user.dir");
		try {
			Document doc = reader.read(new File(dir, fileName));
			Element root = doc.getRootElement();

			List<Element> list = root.elements();
			for (Element e : list) {
				String name = e.getName();
				ConfigOption<?> option = ConfigOption.valueOf(name);
				if (option != null) {
					Object value = null;
					if (option == ConfigOption.CONF_SERVER_ID) {
						value = Integer.valueOf(e.getStringValue());
					} else if (option == ConfigOption.CONF_SERVER_IP) {
						value = e.getStringValue();
					} else if (option == ConfigOption.CONF_SERVER_PORT) {
						value = Integer.valueOf(e.getStringValue());
					} else if (option == ConfigOption.CONF_WORKER_COUNT) {
						value = Integer.valueOf(e.getStringValue());
					}

					if (value != null) {
						configs.put(option, value);
					}
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		loadConfig("config.xml");
		System.out.println(getConfig(ConfigOption.CONF_SERVER_ID));
		System.out.println(getConfig(ConfigOption.CONF_SERVER_IP));
		System.out.println(getConfig(ConfigOption.CONF_SERVER_PORT));
		System.out.println(getConfig(ConfigOption.CONF_WORKER_COUNT, 123));
		System.out.println(getConfig(ConfigOption.CONF_WORKER_COUNT));
	}

}
