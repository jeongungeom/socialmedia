package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Notifications;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationsMapper {
    void insertNotifications(Notifications notifications);
    void updateNotifications(Long id);
    List<Notifications> getNotificationList(Long receiverId);
    void deleteAllNoti(Long receiverId);
}
