package com.sns.socialmedia.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private int id;
    private String username;
    private String email;
    private String bio;
    private String profilePicture;
    private int postCount;
    private int followerCount;
    private int followingCount;

}
