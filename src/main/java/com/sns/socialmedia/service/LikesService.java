package com.sns.socialmedia.service;

import com.sns.socialmedia.mapper.LikesMapper;
import com.sns.socialmedia.model.Likes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesMapper likesMapper;

    // 좋아요 등록
    public void insertLike(Likes like) {
        likesMapper.insertLike(like);
    }

    // 좋아요 취소
    public void deleteLike(Long userId, Long postId) {
        likesMapper.deleteLike(userId, postId);
    }

    // 게시물별 좋아요 수 조회
    public int countByPostId(Long postId) {
        return likesMapper.countByPostId(postId);
    }

    // 사용자가 누른 좋아요 목록 조회
    public List<Likes> findByUserId(Long userId) {
        return likesMapper.findByUserId(userId);
    }

}
