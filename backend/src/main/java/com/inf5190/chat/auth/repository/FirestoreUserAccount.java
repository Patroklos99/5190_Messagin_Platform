package com.inf5190.chat.auth.repository;

import java.util.Objects;

public class FirestoreUserAccount {
    private String username;
    private String encodedPassword;
    public FirestoreUserAccount() {
    }
    public FirestoreUserAccount(String username, String encodedPassword) {
        this.username = username;
        this.encodedPassword = encodedPassword;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEncodedPassword() {
        return encodedPassword;
    }
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.username == null ? 0 : this.username.hashCode());
        hash = 31 * hash + (this.encodedPassword == null ? 0 : this.encodedPassword.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;

        FirestoreUserAccount firestoreUserAccount = (FirestoreUserAccount) obj;
        return Objects.equals(this.username,firestoreUserAccount.username) &&
                Objects.equals(this.encodedPassword,firestoreUserAccount.encodedPassword);
    }

    @Override
    public String toString() {
        return this.username + ", " + this.encodedPassword;
    }
}
