package com.sns.socialmedia.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDto {
    private int id;
    private String imageUrl;
    private String caption;
}
