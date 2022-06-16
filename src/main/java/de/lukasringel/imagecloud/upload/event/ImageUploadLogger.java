package de.lukasringel.imagecloud.upload.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageUploadLogger implements ApplicationListener<ImageUploadedEvent> {
  @Override
  public void onApplicationEvent(ImageUploadedEvent event) {
    log.info("Added image: " + event.imageId());
  }
}