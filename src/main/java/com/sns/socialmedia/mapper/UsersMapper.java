package com.sns.socialmedia.mapper;

import com.sns.socialmedia.Dto.PhotoDto;
import com.sns.socialmedia.Dto.ProfileDto;
import com.sns.socialmedia.model.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UsersMapper {
    int insertUser(Users users); // 회원가입
    Users Login(String username, String password); // 로그인
    int updateUser(Users users); // 정보 수정
    int deleteUser(Long id); // 삭제
    ProfileDto findById(Long id); // id으로 사용자 조회
    PhotoDto getMyPhoto(Long id);
    Optional<Users> findByUsername(String username); // username으로 사용자 조회
    Optional<Users> findByUsernameNotMe(Users users); // username으로 사용자 조회
    Optional<Users> findByEmail(String email); // 이메일로 사용자 조회 (비밀번호 재설정 시 사용)
    List<Users> searchByUsername(String keyword); // 사용자 검색
}
