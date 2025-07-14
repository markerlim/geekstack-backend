package com.geekstack.cards.model;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class FirebaseUser implements Authentication {
    private final FirebaseToken token;
    private boolean authenticated = true;

    // Nested Principal class for @AuthenticationPrincipal
    public static class Principal {
        private final String uid;
        private final String name;
        private final String email;
        private final String picture;

        public Principal(FirebaseToken token) {
            this.uid = token.getUid();
            this.name = token.getName();
            this.email = token.getEmail();
            this.picture = token.getPicture();
        }

        // Getters
        public String getUid() { return uid; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPicture() { return picture; }
    }

    public FirebaseUser(FirebaseToken token) {
        this.token = Objects.requireNonNull(token);
    }

    // Firebase-specific methods
    public String getUid() {
        return token.getUid();
    }

    public String getName() {
        return token.getName();
    }

    public String getEmail() {
        return token.getEmail();
    }

    public String getPicture() {
        return token.getPicture();
    }

    // Spring Security methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return new Principal(token); // Return Principal object instead of just UID
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}