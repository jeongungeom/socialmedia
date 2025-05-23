package com.sns.socialmedia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.socialmedia.mapper.NotificationsMapper;
import com.sns.socialmedia.model.Notifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationsMapper notificationsMapper;

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();


    public void insertNotification(Notifications notifications) {
        notificationsMapper.insertNotifications(notifications);
    }

    public void updateNotification(Long id) {
        notificationsMapper.updateNotifications(id);
    }

    public List<Notifications> getNotificationList(Long id) {
        return notificationsMapper.getNotificationList(id);
    }

    public void deleteAllNoti(Long receiverId) {
        notificationsMapper.deleteAllNoti(receiverId);
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        return emitter;
    }

    public void sendNotification(Long receiverId, String message, Notifications notifications) {
        // 1. 알림 DB에 저장 (생략 or 구현)
        notificationsMapper.insertNotifications(notifications);

        Map<String, Object> alarmData = new HashMap<>();
        Long notificationId = notifications.getId();
        alarmData.put("id", notificationId);
        alarmData.put("message", message);
        alarmData.put("createdAt", notifications.getCreatedAt());

        // 2. 실시간 알림 전송
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(alarmData);

            SseEmitter emitter = emitters.get(receiverId);
            if (emitter != null) {
                emitter.send(SseEmitter.event().name("alarm").data(json));
            }
        } catch (Exception e) {
            emitters.remove(receiverId);
        }
    }
}
