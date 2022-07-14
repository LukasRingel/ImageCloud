package de.lukasringel.imagecloud;

public record ImageCloudSettings(
  String authenticationKey,
  String bucketName,
  String baseUrl,
  String redisUrl,
  String maxmindDownloadUrl
) {
}