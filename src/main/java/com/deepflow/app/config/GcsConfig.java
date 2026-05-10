package com.deepflow.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("gcp")
public class GcsConfig {

    @Bean
    public Storage gcsStorage(
            @Value("${gcp.storage.project-id}") String projectId,
            @Value("${gcp.storage.credentials}") String encodedCredentials
    ) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encodedCredentials);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decoded));
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
