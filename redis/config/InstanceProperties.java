package com.optum.micro.redis.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dataauthorisation.instance.roles")
public class InstanceProperties {
	private final Map<String, String> map = new HashMap<>();

	public Map<String, String> getMap() {
		return this.map;
	}

}
