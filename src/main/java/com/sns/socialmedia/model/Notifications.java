package com.sns.socialmedia.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {
    private Long id;
    private Long receiverId;
    private Long senderId;
    private String type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    public void save(Long receiverId, Long senderId, String type, String message) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.type = type;
        this.message = message;
    }
}
