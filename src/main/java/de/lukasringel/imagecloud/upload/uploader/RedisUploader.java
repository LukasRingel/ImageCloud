package de.lukasringel.imagecloud.upload.uploader;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUploader implements Uploader {
  private final RedissonReactiveClient redisClient;

  @Override
  public CompletableFuture<String> upload(byte[] content, String imageId) {
    return redisClient
      .getBucket("image-cloud:" + imageId)
      .set(
        content,
        15,
        TimeUnit.MINUTES
      )
      .toFuture()
      .thenApply(unused -> imageId);
  }
}