package de.lukasringel.imagecloud.view;

import de.lukasringel.imagecloud.ImageCloudSettings;
import de.lukasringel.imagecloud.view.downloader.GoogleCloudDownloader;
import de.lukasringel.imagecloud.view.downloader.RedisDownloader;
import de.lukasringel.imagecloud.view.event.ImageViewedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ViewController {
  private final ImageCloudSettings settings;
  private final RedisDownloader redisDownloader;
  private final GoogleCloudDownloader cloudDownloader;
  private final ApplicationEventPublisher eventPublisher;

  @GetMapping("/")
  public String defaultPage() {
    return "Invalid image";
  }

  @GetMapping("/{requestedId}")
  public CompletableFuture<ResponseEntity<Resource>> viewImage(@PathVariable String requestedId) {
    var imageId = formatToValidImageId(requestedId);

    if (!isValidImageId(imageId)) {
      return CompletableFuture.completedFuture(
        redirectToBaseUrl()
      );
    }

    return CompletableFuture.supplyAsync(() -> {
      try {
        var cachedImage = redisDownloader.download(imageId).get();
        if (cachedImage != null) {
          eventPublisher.publishEvent(new ImageViewedEvent(
            this,
            imageId,
            cachedImage,
            ImageViewedEvent.ImageSource.REDIS
          ));
          return sendValidImage(cachedImage);
        }

        var storageImage = cloudDownloader.download(imageId).get();
        if (storageImage != null) {
          eventPublisher.publishEvent(new ImageViewedEvent(
            this,
            imageId,
            storageImage,
            ImageViewedEvent.ImageSource.GOOGLE_CLOUD
          ));
          return sendValidImage(storageImage);
        }

        return redirectToBaseUrl();
      } catch (Throwable e) {
        return redirectToBaseUrl();
      }
    });
  }

  private ResponseEntity<Resource> sendValidImage(byte[] content) {
    return ResponseEntity
      .ok()
      .contentType(MediaType.IMAGE_PNG)
      .body(new ByteArrayResource(content));
  }

  private ResponseEntity<Resource> redirectToBaseUrl() {
    var httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(
      URI.create(settings.baseUrl())
    );
    return new ResponseEntity<>(
      httpHeaders,
      HttpStatus.MOVED_PERMANENTLY
    );
  }

  private boolean isValidImageId(String imageId) {
    try {
      UUID.fromString(imageId);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private String formatToValidImageId(String raw) {
    return raw.replace(
      ".png",
      ""
    );
  }
}