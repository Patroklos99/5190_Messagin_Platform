package com.inf5190.chat.messages.model;

import com.google.cloud.Timestamp;

/**
 * Représente un message.
 */
public record Message(String id, String username, Long timestamp, String text, String imageUrl) {
}

;