package com.sns.socialmedia.service;

import com.sns.socialmedia.mapper.FollowsMapper;
import com.sns.socialmedia.model.Follows;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowsService {
    private final FollowsMapper followsMapper;

    public void follow(Long followerId, Long followingId) {
        followsMapper.insertFollow(followerId, followingId);
    }

    public void unfollow(Long followerId, Long followingId) {
        followsMapper.deleteFollow(followerId, followingId);
    }

    public List<Follows> getFollowers(Long userId) {
        return followsMapper.selectFollowers(userId);
    }

    public List<Follows> getFollowings(Long userId) {
        return followsMapper.selectFollowings(userId);
    }

    public int getFollowerCount(Long userId) {
        return followsMapper.countFollowers(userId);
    }

    public int getFollowingCount(Long userId) {
        return followsMapper.countFollowings(userId);
    }

    public boolean isFollowing(Long myId, Long userId) {
        return followsMapper.countByFollowerAndFollowing(myId, userId) > 0;
    }

}
