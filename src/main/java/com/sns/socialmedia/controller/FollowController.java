package com.sns.socialmedia.controller;


import com.sns.socialmedia.model.Follows;
import com.sns.socialmedia.service.FollowsService;
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

    @PostMapping("/{userId}")
    public void follow(@PathVariable Long userId, @RequestParam Long myId) {
        followsService.follow(myId, userId);
    }

    @DeleteMapping("/{userId}")
    public void unfollow(@PathVariable Long userId, @RequestParam Long myId) {
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
}
