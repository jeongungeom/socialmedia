package com.sns.socialmedia.controller;

import com.sns.socialmedia.model.Notifications;
import com.sns.socialmedia.service.NotificationsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationsService notificationsService;

    // 알람 추가
    @PostMapping("/insertNoti")
    public void insertNotification(Notifications notifications) {
        notificationsService.insertNotification(notifications);
    }

    // 알람 삭제
    @DeleteMapping("/deleteNoti")
    public void deleteNotification(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        notificationsService.deleteNotification(id);
    }

    // 알람 조회
    @GetMapping("/getNoti")
    public List<Notifications> selectNotification(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return notificationsService.selectNotifications(id);
    }


}
