package com.optum.micro.redis.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import com.optum.micro.domain.member.das.DataAccessConfig;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;

@Configuration
@EnableCaching
@EnableRedisRepositories(basePackages = "com.optum.micro.redis.repositories")
@Profile(value ="perf")
public class DataAccessValidationCacheConfiguration {
	
	@Value("${azure.rediscache.uri}")
	private String hostName;
	
	@Value("${azure.rediscache.port}")
	private String port;
	
	@Value("${azure.rediscache.password}")
	private String password;
	
	@Value("${azure.rediscache.connection.timeout:60}")
	private long timeout;

	/**
	 * This method creates LettuceConnectionFactory and set configuration details likes hostName, port and password
	 * @return LettuceConnectionFactory
	 */
	@SuppressWarnings("deprecation")
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
			
		   final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(timeout, TimeUnit.SECONDS).build();
		   final ClientOptions clientOptions =
		           ClientOptions.builder().socketOptions(socketOptions).build();

		   LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
//				   .clientOptions(clientOptions).build();
		           .clientOptions(clientOptions).useSsl().build();
//		   RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(hostName,
//				   Integer.valueOf(port));
//		   serverConfig.setPassword(password);
//		   final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig,
//		           clientConfig);
//		   lettuceConnectionFactory.setValidateConnection(true);
//		   return new LettuceConnectionFactory(serverConfig, clientConfig);
		   RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
		   clusterConfiguration.clusterNode(hostName,
				   Integer.valueOf(port));
		   clusterConfiguration.setPassword(password);
		   new LettuceConnectionFactory(clusterConfiguration);
		   final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfiguration,
		           clientConfig);
		   lettuceConnectionFactory.setValidateConnection(true);
		   return new LettuceConnectionFactory(clusterConfiguration, clientConfig);
	}
	/**
	 * This method creates RedisTemplate which do not need external serialization and does it on its own
	 * @return RedisTemplate which binds with Crud Repository to provide querying facilities on cache
	 */
	@Bean
	public RedisTemplate<String, DataAccessConfig> redisTemplate() {
		RedisTemplate<String, DataAccessConfig> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory());
		return template;
	}

}
