package com.sns.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String username;
    private String profilePicture;

    public Comments(String commentText, long userId, long photoId) {
        this.commentText = commentText;
        this.userId = userId;
        this.photoId = photoId;
    }
}
