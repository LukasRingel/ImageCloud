package de.lukasringel.imagecloud;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageOptions;
import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class ImageCloudBeans {
  private ImageCloudSettings settings;

  @PostConstruct
  public void loadSettings() throws IOException {
    var properties = new Properties();
    properties.load(
      new FileInputStream(
        ResourceUtils.getFile("./application.properties")
      )
    );
    settings = new ImageCloudSettings(
      properties.getProperty("image-cloud-authentication-key"),
      properties.getProperty("image-cloud-bucket-name"),
      properties.getProperty("image-cloud-base-url"),
      properties.getProperty("image-cloud-redis-url"),
      properties.getProperty("image-cloud-maxmind-url")
    );
  }

  @Bean
  public ImageCloudSettings settings() {
    return settings;
  }

  @Bean
  public RedissonReactiveClient redisClient() {
    var config = new Config();
    config.useSingleServer().setAddress("redis://" + settings.redisUrl());
    return Redisson.create(config).reactive();
  }

  @Bean
  public Bucket bucket() {
    return StorageOptions
      .getDefaultInstance()
      .getService()
      .get(settings.bucketName());
  }
}