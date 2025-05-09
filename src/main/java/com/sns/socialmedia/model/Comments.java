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
public class Comments {
    private long id;
    private String commentText;
    private long userId;
    private long photoId;
    private LocalDateTime createdAt;
}
