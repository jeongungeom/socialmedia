package com.sns.socialmedia.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    FOLLOW("팔로우"),
    LIKE("좋아요"),
    COMMENT("댓글");

    private final String label;

    NotificationType(String label) {
        this.label = label;
    }

}
