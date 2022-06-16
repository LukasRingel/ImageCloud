package de.lukasringel.imagecloud.upload.uploader;

import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class GoogleCloudUploader implements Uploader {
  private final Bucket bucket;

  @Override
  public CompletableFuture<String> upload(byte[] content, String imageId) {
    return CompletableFuture.supplyAsync(() -> {
      bucket.create(imageId, content);
      return imageId;
    });
  }
}
