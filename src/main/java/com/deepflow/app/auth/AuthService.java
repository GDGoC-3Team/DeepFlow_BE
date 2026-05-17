package com.deepflow.app.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public FirebaseToken verifyIdToken(String idToken) {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid Firebase ID token");
        }
    }

    public FirebaseToken verifyGoogleIdToken(String idToken) {
        FirebaseToken token = verifyIdToken(idToken);
        Object firebaseClaim = token.getClaims().get("firebase");
        if (!(firebaseClaim instanceof Map<?, ?> firebaseData)) {
            throw new IllegalArgumentException("Missing Firebase provider information");
        }

        Object provider = firebaseData.get("sign_in_provider");
        if (!"google.com".equals(provider)) {
            throw new IllegalArgumentException("The provided Firebase ID token is not for Google sign-in");
        }

        return token;
    }
}
