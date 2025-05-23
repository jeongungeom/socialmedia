package com.sns.socialmedia.controller;

import com.sns.socialmedia.enums.NotificationType;
import com.sns.socialmedia.model.Comments;
import com.sns.socialmedia.model.Notifications;
import com.sns.socialmedia.service.CommentsService;
import com.sns.socialmedia.service.NotificationsService;
import com.sns.socialmedia.service.PhotoService;
import com.sns.socialmedia.service.UsersService;
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
    private final PhotoService photoService;
    private final NotificationsService notificationsService;
    private final UsersService usersService;

    @PostMapping("/insertComment")
    public void insertComment(HttpServletRequest request, @RequestBody Comments comments)  {
        Long myId = (Long) request.getAttribute("id");
        comments.setUserId(myId);
        Notifications notifications = new Notifications();

        int userId = photoService.getUserIdByPhotoId(comments.getPhotoId());
        String username = usersService.getUsernameById(myId);
        if(myId != userId) {
            notifications.save((long) userId, myId, NotificationType.COMMENT, username+"님이 댓글을 남겼습니다."+comments.getCommentText());
            notificationsService.insertNotification(notifications);
        }
        commentsService.insertComment(comments);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        commentsService.deleteComments(commentId);
    }

}
