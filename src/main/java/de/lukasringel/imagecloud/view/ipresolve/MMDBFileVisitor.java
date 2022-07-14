package de.lukasringel.imagecloud.view.ipresolve;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MMDBFileVisitor implements FileVisitor<Path> {
  private final Consumer<Path> fileFoundConsumer;

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    Objects.requireNonNull(dir);
    Objects.requireNonNull(attrs);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws MalformedURLException {
    if (!file.getFileName().toUri().toURL().toString().endsWith(".mmdb")) {
      return FileVisitResult.CONTINUE;
    }
    fileFoundConsumer.accept(file);
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    Objects.requireNonNull(file);
    throw exc;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    Objects.requireNonNull(dir);
    if (exc != null) {
      throw exc;
    }
    return FileVisitResult.CONTINUE;
  }
}