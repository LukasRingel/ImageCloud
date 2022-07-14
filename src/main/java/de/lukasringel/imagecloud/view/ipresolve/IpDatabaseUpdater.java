package de.lukasringel.imagecloud.view.ipresolve;

import com.maxmind.geoip2.DatabaseReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.FileType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class IpDatabaseUpdater {
  private static final String ARCHIVE_NAME = "archive.tar.gz";
  private static final String FILE_NAME = "GeoLite2-City.mmdb";

  private final String downloadUrl;

  @SneakyThrows
  public void update() {
    Files.deleteIfExists(Path.of(FILE_NAME));
    var archive = downloadToArchive();
    unarchive(archive);
    renameDatabaseFile();
    FileUtils.deleteDirectory(new File("tmp"));
  }

  @SneakyThrows
  public DatabaseReader databaseReader() {
    return new DatabaseReader.Builder(
      new File(FILE_NAME)
    ).build();
  }

  private File downloadToArchive() throws IOException {
    var archiveFile = new File(ARCHIVE_NAME);
    var downloadStream = new URL(
      downloadUrl
    ).openStream();

    Files.copy(
      downloadStream,
      archiveFile.toPath()
    );

    downloadStream.close();
    return archiveFile;
  }

  public void unarchive(File file) throws IOException {
    var archiver = ArchiverFactory.createArchiver(
      FileType.get(file)
    );
    var tmp = new File("tmp");
    archiver.extract(
      file,
      tmp
    );
    file.delete();
  }

  public void renameDatabaseFile() throws IOException {
    Files.walkFileTree(
      Paths.get("./"),
      new MMDBFileVisitor(path ->
        path.toFile().renameTo(new File(FILE_NAME))
      )
    );
  }


  public static IpDatabaseUpdater createAndUpdate(String downloadUrl) {
    var updater = new IpDatabaseUpdater(downloadUrl);
    updater.update();
    return updater;
  }
}