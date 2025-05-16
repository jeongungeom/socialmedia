package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Follows;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowsMapper {
    int insertFollow(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    int deleteFollow(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    List<Follows> selectFollowers(@Param("userId") Long userId);   // 나를 팔로우하는 사람
    List<Follows> selectFollowings(@Param("userId") Long userId);  // 내가 팔로우하는 사람
    int countFollowers(@Param("userId") Long userId);
    int countFollowings(@Param("userId") Long userId);
}
