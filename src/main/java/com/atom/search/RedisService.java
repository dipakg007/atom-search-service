package com.atom.search;

import com.atom.dto.LocationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  @Value("${spring.redis.ttl}")
  private Integer redisTtl;

  public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  public void saveData(String key, List<LocationDTO> value) {
    try {
      String stringValue = objectMapper.writeValueAsString(value);
      redisTemplate.opsForValue().set(key, stringValue, redisTtl, TimeUnit.SECONDS);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public List<LocationDTO> getData(String key) {
    String stringValue = (String) redisTemplate.opsForValue().get(key);
    if (stringValue == null) return null;
    try {
      return objectMapper.readValue(stringValue, new TypeReference<List<LocationDTO>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
