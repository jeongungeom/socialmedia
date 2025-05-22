package com.sns.socialmedia.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedDto {
    private long id;
    private long userId;
    private String imageUrl;
    private String caption;
    private String username;
    private String profilePicture;
    private int likeCnt;
    private int isLike;
}
