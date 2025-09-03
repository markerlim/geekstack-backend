package com.geekstack.cards.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
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
}