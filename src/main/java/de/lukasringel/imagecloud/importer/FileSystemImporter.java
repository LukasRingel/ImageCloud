package de.lukasringel.imagecloud.importer;

import de.lukasringel.imagecloud.upload.uploader.GoogleCloudUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileSystemImporter {
  private final GoogleCloudUploader uploader;

  @PostConstruct
  public void uploadFromDirectory() throws IOException {
    var path = new File("./imports").toPath();

    if (!Files.exists(path)) {
      return;
    }

    Files.walkFileTree(
      path,
      new ImageFileVisitor((imagePath, imageId) -> {
        try {
          uploader.upload(
            Files.readAllBytes(imagePath),
            imageId
          );
          Files.delete(imagePath);
          log.info("Imported image: " + imageId);
        } catch (IOException e) {
          e.printStackTrace();
        }
      })
    );
  }
}