package kr.lineedu.lms.config.redis;

import kr.lineedu.lms.config.properties.ApplicationProperties;
import kr.lineedu.lms.config.properties.ApplicationProperties.Redis;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//for redis connection
@Configuration
@EnableCaching
public class RedisConfig {
	private final Environment environment;

	public RedisConfig(ApplicationProperties applicationProperties, Environment environment) {
		this.environment = environment;
		// Fallback to Environment if ApplicationProperties.redis() is null
		if (applicationProperties.redis() == null) {
			// Properties will be read from Environment in redisConnectionFactory()
		}
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		
		// Try to get from ApplicationProperties first, fallback to Environment
		String host = environment.getProperty("spring.redis.host", "127.0.0.1");
		String port = environment.getProperty("spring.redis.port", "6380");
		String password = environment.getProperty("spring.redis.password", "");
		
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(Integer.parseInt(port));
		if (password != null && !password.isEmpty()) {
			redisStandaloneConfiguration.setPassword(password);
		}
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(
			redisStandaloneConfiguration);
		return lettuceConnectionFactory;
	}

	//redis template
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
		stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
		return stringRedisTemplate;
	}
}
