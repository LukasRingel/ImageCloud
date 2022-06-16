package de.lukasringel.imagecloud.view.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageViewedLogger implements ApplicationListener<ImageViewedEvent> {
  @Override
  public void onApplicationEvent(ImageViewedEvent event) {
    log.info("Viewed image (%s): %s".formatted(
      event.imageSource().name(),
      event.imageId()
    ));
  }
}