package com.sns.socialmedia.service;

import com.sns.socialmedia.mapper.NotificationsMapper;
import com.sns.socialmedia.model.Notifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationsMapper notificationsMapper;

    public void insertNotification(Notifications notifications) {
        notificationsMapper.insertNotifications(notifications);
    }

    public void deleteNotification(Long id) {
        notificationsMapper.deleteNotifications(id);
    }

    public List<Notifications> selectNotifications(Long id) {
        return notificationsMapper.selectNotifications(id);
    }
}
