package de.lukasringel.imagecloud.view.event;

import de.lukasringel.imagecloud.view.ipresolve.LocationByIpResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageViewedLogger implements ApplicationListener<ImageViewedEvent> {
  private final LocationByIpResolver locationByIpResolver;

  @Override
  public void onApplicationEvent(ImageViewedEvent event) {
    var geoLocation = locationByIpResolver.resolveGeoLocation(
      event.requestAddress()
    );
    log.info("Viewed image (%s|%s/%s): %s".formatted(
      event.requestAddress()
        .toString()
        .replace("/", ""),
      geoLocation.getCity().getName(),
      geoLocation.getCountry().getName(),
      event.imageId()
    ));
  }
}