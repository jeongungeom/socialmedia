package com.sns.socialmedia.controller;

import com.sns.socialmedia.model.Comments;
import com.sns.socialmedia.service.CommentsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentsService commentsService;

    @PostMapping("/insertComment")
    public void insertComment(HttpServletRequest request, @RequestBody Comments comments)  {
        Long myId = (Long) request.getAttribute("id");
        comments.setUserId(myId);
        commentsService.insertComment(comments);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        commentsService.deleteComments(commentId);
    }

}
