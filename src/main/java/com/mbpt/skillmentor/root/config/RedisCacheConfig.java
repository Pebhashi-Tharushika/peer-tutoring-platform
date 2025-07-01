package com.mbpt.skillmentor.root.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // 1. Configure ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Auto registers modules like Jdk8Module, JavaTimeModule, KotlinModule, etc. // handles Java 8 time, etc.
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignore unknown properties during deserialization
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Serialize dates as ISO 8601 strings (e.g., "2023-01-01T12:00:00Z")

        // 2. Create Jackson2JsonRedisSerializer with the configured ObjectMapper
        // The setObjectMapper() method is deprecated. so, pass objectMapper directly to the constructor
        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // 3. Configure RedisCacheConfiguration
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // Default TTL for cache entries
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer) // Values serialized with Jackson
                );

        // 4. Build RedisCacheManager
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config) // Apply the default configuration
                .build();
    }
}
