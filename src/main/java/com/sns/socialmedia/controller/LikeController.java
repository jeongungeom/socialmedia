package com.sns.socialmedia.controller;

import com.sns.socialmedia.model.Likes;
import com.sns.socialmedia.service.LikesService;
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

    // 좋아요 추가
    @PostMapping("/addLike")
    public void addLike(HttpServletRequest request, @RequestBody  Likes likes) {
        likes.setUserId((Long) request.getAttribute("id"));
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
