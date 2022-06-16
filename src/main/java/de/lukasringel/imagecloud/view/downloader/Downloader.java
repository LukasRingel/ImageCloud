package de.lukasringel.imagecloud.view.downloader;

import java.util.concurrent.CompletableFuture;

public interface Downloader {
  CompletableFuture<byte[]> download(String imageId);
}