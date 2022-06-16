package de.lukasringel.imagecloud.upload.uploader;

import java.util.concurrent.CompletableFuture;

public interface Uploader {
  CompletableFuture<String> upload(byte[] content, String imageId);
}