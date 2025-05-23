package com.sns.socialmedia.controller;

import com.sns.socialmedia.enums.NotificationType;
import com.sns.socialmedia.model.Likes;
import com.sns.socialmedia.model.Notifications;
import com.sns.socialmedia.service.LikesService;
import com.sns.socialmedia.service.NotificationsService;
import com.sns.socialmedia.service.PhotoService;
import com.sns.socialmedia.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikesService likesService;
    private final PhotoService photoService;
    private final NotificationsService notificationsService;
    private final UsersService usersService;
    // 좋아요 추가
    @PostMapping("/addLike")
    public void addLike(HttpServletRequest request, @RequestBody  Likes likes) {
        Long myId = (Long) request.getAttribute("id");
        likes.setUserId(myId);
        Notifications notifications = new Notifications();

        int userId = photoService.getUserIdByPhotoId(likes.getPhotoId());
        String username = usersService.getUsernameById(myId);

        if(myId != userId) {
            notifications.save((long) userId, myId, NotificationType.LIKE, username+"님이 회원님의 사진을 좋아합니다.");
        }
        notificationsService.insertNotification(notifications);
        likesService.insertLike(likes);
    }

    // 좋아요 삭제
    @DeleteMapping("/deleteLike/{photoId}")
    public void deleteLike(HttpServletRequest request, @PathVariable("photoId") Long photoId) {
        Long userId = (Long) request.getAttribute("id");
        likesService.deleteLike(userId, photoId);
    }

    @GetMapping("likeList/{photoId}")
    public List<Likes> likesList(@PathVariable("photoId") Long photoId) {
        return likesService.findByUserId(photoId);
    }
}
