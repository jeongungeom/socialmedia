package com.sns.socialmedia.controller;

import com.sns.socialmedia.model.Notifications;
import com.sns.socialmedia.service.NotificationsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationsService notificationsService;

    // 알람 조회
    @GetMapping("/getNoti")
    public List<Notifications> getNotificationList(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return notificationsService.getNotificationList(id);
    }

    // 알람 확인
    @PutMapping("/readNoti/{notiId}")
    public void readNotification(@PathVariable("notiId") Long notiId) {
        notificationsService.updateNotification(notiId);
    }

    // 전체 확인
    @PutMapping("/deleteAllNoti")
    public void deleteAllNoti(HttpServletRequest request) {
        Long myId = (Long) request.getAttribute("id");
        notificationsService.deleteAllNoti(myId);
    }

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationsService.subscribe(userId);
    }
}
