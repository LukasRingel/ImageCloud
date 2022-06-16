package de.lukasringel.imagecloud.view.event;

import de.lukasringel.imagecloud.upload.uploader.RedisUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageViewedCacher implements ApplicationListener<ImageViewedEvent> {
  private final RedisUploader redisUploader;

  @Override
  public void onApplicationEvent(ImageViewedEvent event) {
    redisUploader.upload(
      event.content(),
      event.imageId()
    );
  }
}