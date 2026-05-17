package com.deepflow.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials-path:}")
    private String firebaseCredentialsPath;

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        if (FirebaseApp.getApps().isEmpty()) {
            if (StringUtils.hasText(firebaseCredentialsPath)) {
                try (InputStream inputStream = new FileInputStream(firebaseCredentialsPath)) {
                    GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(credentials)
                            .build();
                    return FirebaseApp.initializeApp(options);
                }
            }

            ClassPathResource resource = new ClassPathResource("firebase-credentials.json");
            if (!resource.exists()) {
                throw new IllegalStateException(
                        "Firebase credentials are required via firebase.credentials-path or firebase-credentials.json on the classpath"
                );
            }
            try (InputStream inputStream = resource.getInputStream()) {
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();
                return FirebaseApp.initializeApp(options);
            }
        }
        return FirebaseApp.getInstance();
    }
}
