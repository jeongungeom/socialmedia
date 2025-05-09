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
    private long follwerId;
    private long followingId;
    private LocalDateTime createdAt;
}
