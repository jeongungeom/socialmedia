package com.sns.socialmedia.controller;


import com.sns.socialmedia.model.Follows;
import com.sns.socialmedia.model.Notifications;
import com.sns.socialmedia.service.FollowsService;
import com.sns.socialmedia.service.NotificationsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowsService followsService;

    private final NotificationsService notificationsService;

    @PostMapping("/{userId}")
    public void follow(@PathVariable Long userId, HttpServletRequest request) {
        Long myId = (Long) request.getAttribute("id");
        Notifications notifications = new Notifications();

        notifications.save(userId, myId, "FOLLOW", "님이 당신을 팔로우 했습니다.");
        notificationsService.insertNotification(notifications);
        followsService.follow(myId, userId);
    }

    @DeleteMapping("/{userId}")
    public void unfollow(@PathVariable Long userId, HttpServletRequest request) {
        Long myId = (Long) request.getAttribute("id");
        followsService.unfollow(myId, userId);
    }

    @GetMapping("/followers/{userId}")
    public List<Follows> getFollowers(@PathVariable Long userId) {
        return followsService.getFollowers(userId);
    }

    @GetMapping("/followings/{userId}")
    public List<Follows> getFollowings(@PathVariable Long userId) {
        return followsService.getFollowings(userId);
    }

    @GetMapping("/count/followers/{userId}")
    public int getFollowerCount(@PathVariable Long userId) {
        return followsService.getFollowerCount(userId);
    }

    @GetMapping("/count/followings/{userId}")
    public int getFollowingCount(@PathVariable Long userId) {
        return followsService.getFollowingCount(userId);
    }

    @GetMapping("/isFollowing")
    public boolean isFollowing(@RequestParam Long userId, HttpServletRequest request) {
        Long myId = (Long) request.getAttribute("id");
        if (myId == null || myId.equals(userId)) return false;
        return followsService.isFollowing(myId, userId);
    }
}
