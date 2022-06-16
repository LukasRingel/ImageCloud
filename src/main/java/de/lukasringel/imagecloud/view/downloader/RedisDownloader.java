package de.lukasringel.imagecloud.view.downloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDownloader implements Downloader {
  private final RedissonReactiveClient redisClient;

  @Override
  public CompletableFuture<byte[]> download(String imageId) {
    var bucket = redisClient.getBucket("image-cloud:" + imageId);
    return bucket
      .isExists()
      .toFuture()
      .thenApply(exists -> {
        if (!exists) {
          return null;
        }
        return (byte[]) bucket.get().block();
      });
  }
}