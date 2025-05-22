package com.sns.socialmedia.service;

import com.sns.socialmedia.mapper.CommentsMapper;
import com.sns.socialmedia.model.Comments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsMapper commentsMapper;

    // 댓글 등록
    public void insertComment(Comments comments) {
        commentsMapper.insertComment(comments);
    }

    // 댓글 삭제
    public void deleteComments(Long id) {
        commentsMapper.deleteComments(id);
    }

}
