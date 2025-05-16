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
public class Follows {
    private long followerId;
    private long followingId;
    private  String followerUsername;
    private  String followingUsername;
    private String followerProfilePicture;
    private String followingProfilePicture;
    private LocalDateTime createdAt;
}
