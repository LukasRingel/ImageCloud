package de.lukasringel.imagecloud.upload.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Accessors(fluent = true)
public class ImageUploadedEvent extends ApplicationEvent {
  private final byte[] content;
  private final String imageId;

  public ImageUploadedEvent(Object source, byte[] content, String imageId) {
    super(source);
    this.content = content;
    this.imageId = imageId;
  }
}