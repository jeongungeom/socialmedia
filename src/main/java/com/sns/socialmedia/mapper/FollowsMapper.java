package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Follows;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowsMapper {
    void insertFollow(Follows follow); // 팔로우 등록
    void deleteFollow(Long followerId, Long followingId); // 팔로우 취소
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId); // 팔로우 관계 존재 여부 확인
    int countByFollowerId(Long followerId); // 팔로잉 수 조회
    int countByFollowingId(Long followingId); // 팔로워 수 조회
    List<Follows> findFollowers(Long userId); // 나를 팔로우하는 사람 목록
    List<Follows> findFollowings(Long userId); // 내가 팔로우하는 사람 목록
}
