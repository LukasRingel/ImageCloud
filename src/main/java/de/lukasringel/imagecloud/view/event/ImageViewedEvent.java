package de.lukasringel.imagecloud.view.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true)
public class ImageViewedEvent extends ApplicationEvent {
  private final String imageId;
  private final byte[] content;
  private final ImageSource imageSource;

  public ImageViewedEvent(Object source, String imageId, byte[] content, ImageSource imageSource) {
    super(source);
    this.imageId = imageId;
    this.content = content;
    this.imageSource = imageSource;
  }

  public enum ImageSource {
    GOOGLE_CLOUD,
    REDIS
  }
}