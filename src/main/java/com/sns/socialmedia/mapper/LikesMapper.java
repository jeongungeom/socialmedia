package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Likes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikesMapper {
    int insertLike(Likes like); // 좋아요 등록
    int deleteLike(Long userId, Long postId); // 좋아요 취소
    int countByPostId(Long photoId); // 게시물별 좋아요 수 조회
    List<Likes> findByUserId(Long userId); // 사용자가 누른 좋아요 목록 조회
}
