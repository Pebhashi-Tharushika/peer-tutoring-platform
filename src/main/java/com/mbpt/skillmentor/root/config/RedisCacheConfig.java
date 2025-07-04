package com.mbpt.skillmentor.root.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbpt.skillmentor.root.dto.StudentDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;

@Configuration
@Profile("prod")
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // 1. Configure ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Auto registers modules like Jdk8Module, JavaTimeModule, KotlinModule, etc. // handles Java 8 time, etc.
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // Ignore unknown properties during deserialization
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Serialize dates as ISO 8601 strings (e.g., "2023-01-01T12:00:00Z")

        // 2. Create specific serializers for each type you're caching
        Jackson2JsonRedisSerializer<StudentDTO> studentDtoSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, StudentDTO.class);

        // For lists/collections, you need to tell Jackson about the generic type
        Jackson2JsonRedisSerializer<List<StudentDTO>> studentDtoListSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, objectMapper.getTypeFactory().constructCollectionType(List.class, StudentDTO.class));


        // 3. Define default cache configuration (for caches not explicitly configured)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // Default TTL for cache entries
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, Object.class)) // Values serialized with Jackson
                );

        // 4. Define specific cache configurations for your named caches
        // Configuration for "studentCache"
        RedisCacheConfiguration studentCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // TTL for individual students
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(studentDtoSerializer)); // Use specific serializer for StudentDTO

        // Configuration for "allStudentsCache"
        RedisCacheConfiguration allStudentsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // TTL for all students list
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(studentDtoListSerializer)); // Use specific serializer for List<StudentDTO>


        // 5. Build RedisCacheManager with specific configs
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig) // Apply default for un-configured caches
                .withCacheConfiguration("studentCache", studentCacheConfig) // Apply specific config
                .withCacheConfiguration("allStudentsCache", allStudentsCacheConfig) // Apply specific config
                .build();

    }
}
