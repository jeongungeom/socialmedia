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

    // 팔로우 등록
    public int insertFollow(Follows follows) {
        return followsMapper.insertFollow(follows);
    }

    // 팔로우 취소
    public int deleteFollow(Long followerId, Long followingId) {
        return followsMapper.deleteFollow(followerId, followingId);
    }

    // 팔로잉 수 조회
    public int countByFollowerId(Long followerId) {
        return followsMapper.countByFollowerId(followerId);
    }

    // 팔로워 수 조회
    public int countByFollowingId(Long followingId) {
        return followsMapper.countByFollowingId(followingId);
    }

    // 나를 팔로우하는 사람 목록
    public List<Follows> findFollowers(Long userId) {
        return followsMapper.findFollowers(userId);
    }

    // 내가 팔로우하는 사람 목록
    public List<Follows> findFollowings(Long userId) {
        return followsMapper.findFollowings(userId);
    }
}
