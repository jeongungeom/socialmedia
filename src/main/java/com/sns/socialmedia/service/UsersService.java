package com.sns.socialmedia.service;


import com.sns.socialmedia.exception.DuplicateUserException;
import com.sns.socialmedia.mapper.UsersMapper;
import com.sns.socialmedia.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersMapper usersMapper;

    // 회원가입
    public void checkValid(Users users) {
        if(usersMapper.findByUsername(users.getUsername()).isPresent()) {
            throw new DuplicateUserException("중복된 아이디입니다.");
        }
        usersMapper.insertUser(users);
    }

    // 회원 정보 수정
    public int updateUser(Users users) {
        return usersMapper.updateUser(users);
    }

    // 회원 정보 삭제
    public int deleteUser(Long id) {
        return usersMapper.deleteUser(id);
    }

    // username으로 사용자 조회
    public Optional<Users> findByUsername(String username) {
        return usersMapper.findByUsername(username);
    }

    // 이메일로 사용자 조회 (비밀번호 재설정 시 사용)
    public Optional<Users> findByEmail(String email) {
        return usersMapper.findByEmail(email);
    }

    // 사용자 검색
    public List<Users>  searchByUsername(String keyword) {
        return usersMapper.searchByUsername(keyword);
    }
}
