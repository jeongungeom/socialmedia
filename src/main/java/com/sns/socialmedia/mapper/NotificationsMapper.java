package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Notifications;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NotificationsMapper {
    void insertNotifications(Notifications notifications);
    void deleteNotifications(Long id);
    List<Notifications> selectNotifications(Long receiverId);
}
