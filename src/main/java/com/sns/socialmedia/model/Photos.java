package com.sns.socialmedia.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class Photos {
    private long id;
    private long userId;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;

    public Photos(long userId, String imageUrl, String caption) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.caption = caption;
    }
}
