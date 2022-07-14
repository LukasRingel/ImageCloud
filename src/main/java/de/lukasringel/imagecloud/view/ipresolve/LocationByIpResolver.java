package de.lukasringel.imagecloud.view.ipresolve;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import de.lukasringel.imagecloud.ImageCloudSettings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

@Component
@RequiredArgsConstructor
public class LocationByIpResolver {
  private final ImageCloudSettings settings;

  private DatabaseReader databaseReader;

  @PostConstruct
  public void updateDatabase() {
    var updater = IpDatabaseUpdater.createAndUpdate(
      settings.maxmindDownloadUrl()
    );
    this.databaseReader = updater.databaseReader();
  }

  @SneakyThrows
  public CityResponse resolveGeoLocation(InetAddress inetAddress) {
    return databaseReader.city(inetAddress);
  }
}