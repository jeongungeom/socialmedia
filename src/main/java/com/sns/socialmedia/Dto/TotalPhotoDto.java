package com.sns.socialmedia.Dto;

import com.sns.socialmedia.model.Comments;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TotalPhotoDto {
    private PhotoDto photoDto;
    private List<Comments> commentsList;
}
