package com.inf5190.chat.messages.repository;

import com.google.cloud.Timestamp;
import com.inf5190.chat.auth.repository.FirestoreUserAccount;

import java.util.Objects;


public class FirestoreMessage {
    private String username;
    private Timestamp timestamp;
    private String text;
    private String imageUrl;
    public FirestoreMessage() {
    }
    public FirestoreMessage(String username, Timestamp timestamp, String text, String imageUrl) {
        this.username = username;
        this.timestamp = timestamp;
        this.text = text;
        this.imageUrl = imageUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.username == null ? 0 : this.username.hashCode());
        hash = 31 * hash + (this.timestamp == null ? 0 : this.timestamp.hashCode());
        hash = 31 * hash + (this.text == null ? 0 : this.text.hashCode());
        hash = 31 * hash + (this.imageUrl == null ? 0 : this.imageUrl.hashCode());
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

        FirestoreMessage firestoreMessage = (FirestoreMessage) obj;
        return Objects.equals(this.username,firestoreMessage.username) &&
                Objects.equals(this.timestamp,firestoreMessage.timestamp)&&
                Objects.equals(this.text,firestoreMessage.text)&&
                Objects.equals(this.imageUrl,firestoreMessage.imageUrl);
    }

    @Override
    public String toString() {
        return this.username + ", " + this.timestamp.toString() + ", " + this.text + ", " + this.imageUrl;
    }
}
