package de.lukasringel.imagecloud.view.downloader;

import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class GoogleCloudDownloader implements Downloader {
  private final Bucket bucket;

  @Override
  public CompletableFuture<byte[]> download(String imageId) {
    return CompletableFuture.supplyAsync(() ->
      bucket.get(imageId).getContent()
    );
  }
}