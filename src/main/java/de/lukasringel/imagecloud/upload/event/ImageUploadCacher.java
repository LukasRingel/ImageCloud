package de.lukasringel.imagecloud.upload.event;

import de.lukasringel.imagecloud.upload.uploader.RedisUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUploadCacher implements ApplicationListener<ImageUploadedEvent> {
  private final RedisUploader redisUploader;

  @Override
  public void onApplicationEvent(ImageUploadedEvent event) {
    redisUploader.upload(
      event.content(),
      event.imageId()
    );
  }
}