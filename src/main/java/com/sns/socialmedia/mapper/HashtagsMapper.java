package com.sns.socialmedia.mapper;

import com.sns.socialmedia.model.Hashtags;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HashtagsMapper {
    int insertHashtag(Hashtags hashtag); // 해시태그 등록
    Optional<Hashtags> findById(Long id); // 해시태그 ID로 조회
    Optional<Hashtags> findByName(String name); // 해시태그 이름으로 조회
    List<Hashtags> findAll(); // 전체 해시태그 목록 조회
}
