package com.geekstack.cards.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleCloudStorageService {

    private final Storage storage;

    @Value("${gcs.bucket}")
    private String bucketName;

    public GoogleCloudStorageService() throws IOException {
        GoogleCredentials credentials;

        // Check if the credentials are provided in an environment variable
        String gcsConfig = System.getenv("GOOGLE_CLOUD_STORAGE_CREDENTIALS");

        if (gcsConfig != null && !gcsConfig.isEmpty()) {
            // Load credentials from the environment variable (JSON string)
            credentials = GoogleCredentials.fromStream(
                    new java.io.ByteArrayInputStream(gcsConfig.getBytes(StandardCharsets.UTF_8)));
        } else {
            // Fall back to loading credentials from a local file
            InputStream serviceAccount = new ClassPathResource("google/service-account-key.json").getInputStream();
            credentials = GoogleCredentials.fromStream(serviceAccount);
        }

        // Build the Storage instance using the credentials
        this.storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

    public String uploadImage(byte[] fileData, String userId, String fileName) throws IOException {
        // Dynamically get the content type
        String contentType = Files.probeContentType(Paths.get(fileName));

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, "user-images/" + userId + "/" + fileName)
                .setContentType(contentType)
                .build();

        try {
            // Upload the file to Google Cloud Storage - NO ACL SETTING
            Blob blob = storage.create(blobInfo, fileData);

            // Return the public URL (note: this will only work if bucket is public)
            return "https://storage.googleapis.com/" + bucketName + "/user-images/" + userId + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error uploading file to Google Cloud Storage", e);
        }
    }

    public String uploadPostImage(byte[] fileData, String postId, String fileName) throws IOException {
        // Dynamically get the content type
        String contentType = Files.probeContentType(Paths.get(fileName));

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, "userpost/" + fileName)
                .setContentType(contentType)
                .build();

        try {
            // Upload the file to Google Cloud Storage - NO ACL SETTING
            Blob blob = storage.create(blobInfo, fileData);

            return "https://storage.googleapis.com/" + bucketName + "/userpost/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error uploading file to Google Cloud Storage", e);
        }
    }

    /**
     * Deletes multiple images from Google Cloud Storage given their public URLs.
     * Strips the URL to get the object path and performs batch deletion.
     *
     * @param urls List of public URLs to delete
     * @return true if all deletions succeeded, false otherwise
     */
    public boolean deleteImages(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            System.err.println("No URLs to delete from GCS.");
            return true; // nothing to do
        }
        
        java.util.List<String> objectPaths = new java.util.ArrayList<>();
        for (String url : urls) {
            // Example:
            // https://storage.googleapis.com/{bucket}/user-images/{userId}/{fileName}
            // Strip to get object path after the bucket name
            String prefix = "https://storage.googleapis.com/" + bucketName + "/userpost/";
            if (url.startsWith(prefix)) {
                objectPaths.add("userpost/" + url.substring(prefix.length()));
            } else {
                // If URL format is unexpected, skip or handle accordingly
                System.err.println("Invalid GCS URL: " + url);
            }
        }

        List<BlobId> blobIds = new ArrayList<>();
        for (String objectPath : objectPaths) {
            blobIds.add(BlobId.of(bucketName, objectPath));
        }

        try {
            List<Boolean> results = storage.delete(blobIds);
            return results.stream().allMatch(Boolean::booleanValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error batch deleting files from Google Cloud Storage", e);
        }
    }
}