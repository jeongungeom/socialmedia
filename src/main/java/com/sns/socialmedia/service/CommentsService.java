package com.sns.socialmedia.service;

import com.sns.socialmedia.mapper.CommentsMapper;
import com.sns.socialmedia.model.Comments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsMapper commentsMapper;

    // 댓글 등록
    public int insertComment(Comments comments) {
        return commentsMapper.insertComment(comments);
    }

    // 특정 게시물에 달린 댓글 전체 조회
    public List<Comments> findByPostId(Long postId) {
        return commentsMapper.findByPostId(postId);
    }

    // 댓글 내용 수정
    public int updateComments(Comments comments) {
        return commentsMapper.updateComments(comments);
    }

    // 댓글 삭제
    public int deleteComments(Long id) {
        return commentsMapper.deleteComments(id);
    }

    // 게시물별 댓글 개수 조회
    public int countByPostId(Long postId) {
        return commentsMapper.countByPostId(postId);
    }
}
