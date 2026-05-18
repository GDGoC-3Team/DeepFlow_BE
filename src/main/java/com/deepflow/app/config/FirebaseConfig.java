package com.deepflow.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Slf4j
@Configuration
public class FirebaseConfig {

    private final Environment environment;

    public FirebaseConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = true)
    public FirebaseApp firebaseApp() throws Exception {
        String firebaseCredentialsJson = resolveCredentialsJson();
        String firebaseCredentialsPath = resolveCredentialsPath();

        if (FirebaseApp.getApps().isEmpty()) {
            if (StringUtils.hasText(firebaseCredentialsJson)) {
                try (InputStream inputStream = new ByteArrayInputStream(
                        firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8))) {
                    GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(credentials)
                            .build();
                    return FirebaseApp.initializeApp(options);
                }
            }

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
                log.warn("Firebase initialization skipped because no credentials were found");
                throw new IllegalStateException(
                        "Firebase credentials are required via FIREBASE_CREDENTIALS/FIREBASE_CREDENTIALS_JSON, "
                                + "firebase.credentials-path/FIREBASE_CREDENTIALS_PATH, or firebase-credentials.json on the classpath"
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

    String resolveCredentialsPath() {
        return firstNonBlank(
                environment.getProperty("firebase.credentials-path"),
                environment.getProperty("FIREBASE_CREDENTIALS_PATH"),
                environment.getProperty("FIREBASE_CREDENTIAL_PATH")
        );
    }

    String resolveCredentialsJson() {
        return firstNonBlank(
                environment.getProperty("FIREBASE_CREDENTIALS"),
                environment.getProperty("FIREBASE_CREDENTIALS_JSON"),
                environment.getProperty("FIREBASE_CREDENTIAL")
        );
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return "";
    }
}
