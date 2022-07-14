package de.lukasringel.imagecloud.view.event;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.net.InetAddress;

@Getter
@Accessors(fluent = true)
public class ImageViewedEvent extends ApplicationEvent {
  private final String imageId;
  private final byte[] content;
  private final ImageSource imageSource;
  private final InetAddress requestAddress;

  public ImageViewedEvent(Object source,
                          String imageId,
                          byte[] content,
                          ImageSource imageSource,
                          InetAddress address) {
    super(source);
    this.imageId = imageId;
    this.content = content;
    this.imageSource = imageSource;
    this.requestAddress = address;
  }

  public enum ImageSource {
    GOOGLE_CLOUD,
    REDIS
  }
}