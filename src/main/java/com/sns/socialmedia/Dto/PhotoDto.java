package com.sns.socialmedia.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class PhotoDto {
    private int id;
    private int userId;
    private String imageUrl;
    private String caption;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String username;
    private String profilePicture;
    private int likeCnt;
    private int isLike;

}
