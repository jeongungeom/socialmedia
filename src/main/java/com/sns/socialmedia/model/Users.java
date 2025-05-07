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
public class Users {
    private long id;
    private String username;
    private String email;
    private String password;
    private String bio;
    private String profilePicture;
    private LocalDateTime createAt;
}
