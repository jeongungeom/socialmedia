package com.sns.socialmedia.service;


import com.sns.socialmedia.mapper.PhotosMapper;
import com.sns.socialmedia.model.Photos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotosMapper photosMapper;

    // 게시물 등록
    public void insertPhotos(Photos photo){
        photosMapper.insertPhotos(photo);
    }

    // 게시물 ID로 조회
    public Optional<Photos> findById(Long id){
        return photosMapper.findById(id);
    }

    // 특정 사용자의 게시물 목록 조회
    public List<Photos> findByUserId(Long userId){
        return photosMapper.findByUserId(userId);
    }

    // 전체 게시물 조회
    public List<Photos> findAll(){
        return photosMapper.findAll();
    }

    // 게시물 수정
    public int updatePhotos(Photos photo){
        return photosMapper.updatePhotos(photo);
    }

    // 게시물 삭제
    public void deletePhotos(Long userId, Long id){
        photosMapper.deletePhotos(userId, id);
    }

    // 사용자의 게시물 수 조회
    public int countByUserId(Long userId){
        return photosMapper.countByUserId(userId);
    }

}
