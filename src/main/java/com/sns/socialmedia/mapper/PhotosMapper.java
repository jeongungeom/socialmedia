package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Photos;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PhotosMapper {
    int insertPhotos(Photos photo); // 게시물 등록
    Optional<Photos> findById(Long id); // 게시물 ID로 조회
    List<Photos> findByUserId(Long userId); // 특정 사용자의 게시물 목록 조회
    List<Photos> findAll(); // 전체 게시물 조회
    int updatePhotos(Photos photo); // 게시물 수정
    int deletePhotos(Long userId, Long id); // 게시물 삭제
    int countByUserId(Long userId); // 사용자의 게시물 수 조회
}
