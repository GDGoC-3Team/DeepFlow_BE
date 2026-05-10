package com.deepflow.app.storage;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("gcp")
@RequiredArgsConstructor
public class GcsStorageService implements StorageService {

    private final Storage storage;

    @Value("${gcp.storage.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile file, String key) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(bucket, key)
                    .setContentType(file.getContentType())
                    .build();
            storage.create(blobInfo, file.getBytes());
            return "https://storage.googleapis.com/" + bucket + "/" + key;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Failed to upload file to GCS", exception);
        }
    }
}
