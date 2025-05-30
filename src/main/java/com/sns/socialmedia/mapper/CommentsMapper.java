package com.sns.socialmedia.mapper;


import com.sns.socialmedia.model.Comments;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsMapper {
    int insertComment(Comments comment); // 댓글 등록
    List<Comments> findMyPhotoId(Long photoId); // 특정 게시물에 달린 댓글 전체 조회
    int updateComments(Comments comment); // 댓글 내용 수정
    int deleteComments(Long id); // 댓글 삭제
}
