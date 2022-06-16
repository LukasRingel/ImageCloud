package de.lukasringel.imagecloud.upload;

import de.lukasringel.imagecloud.ImageCloudSettings;
import de.lukasringel.imagecloud.upload.event.ImageUploadedEvent;
import de.lukasringel.imagecloud.upload.uploader.GoogleCloudUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {
  private static final String AUTH_HEADER_NAME = "X-AUTHENTICATION-KEY";

  private final ApplicationEventPublisher eventPublisher;
  private final GoogleCloudUploader uploader;
  private final ImageCloudSettings settings;

  @PostMapping("/action/upload")
  private CompletableFuture<ResponseEntity<String>> uploadImage(@RequestBody byte[] imageContent,
                                                                HttpServletRequest request) {
    if (!isAuthorized(request)) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
      );
    }

    var imageId = UUID.randomUUID().toString();

    return uploader.upload(
      imageContent,
      imageId
    ).whenComplete((s, throwable) ->
      eventPublisher.publishEvent(new ImageUploadedEvent(
        this,
        imageContent,
        imageId
      ))
    ).thenApply(ResponseEntity::ok);
  }

  private boolean isAuthorized(HttpServletRequest request) {
    return request.getHeader(AUTH_HEADER_NAME) != null &&
      request.getHeader(AUTH_HEADER_NAME).equals(settings.authenticationKey());
  }
}