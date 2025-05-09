package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Hashtags;
import com.sns.socialmedia.model.Photos;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoTagsMapper {
    int insertPhotoTag(Long photoId, Long hashtagId); // 게시물-해시태그 연결 등록
    int deletePhotoTag(Long photoId, Long hashtagId); // 게시물-해시태그 연결 삭제
    List<Hashtags> findHashtagsByPhotoId(Long photoId); // 게시물에 달린 해시태그 목록 조회
    List<Photos> findPhotosByHashtagId(Long hashtagId); // 해시태그가 달린 게시물 목록 조회
}
