package com.sns.socialmedia.service;


import com.sns.socialmedia.Dto.PhotoDto;
import com.sns.socialmedia.Dto.ProfileDto;
import com.sns.socialmedia.exception.DuplicateUserException;
import com.sns.socialmedia.mapper.UsersMapper;
import com.sns.socialmedia.model.Users;
import com.sns.socialmedia.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    private final UsersMapper usersMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // 회원가입 및 아이디 중복 체크
    public void checkValid(Users users) {
        if(usersMapper.findByUsername(users.getUsername()).isPresent()) {
            throw new DuplicateUserException("이미 사용중인 아이디입니다.");
        } else if (usersMapper.findByEmail(users.getEmail()).isPresent()) {
            throw new DuplicateUserException("이미 사용중인 이메일입니다.");
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
       usersMapper.insertUser(users);
    }

    // 로그인 및 JWT 발급
    public String login(String username, String rawPassword) {
        Users user = usersMapper.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 발급
        return jwtUtil.generateToken(user.getId(), user.getUsername());

    }

    // 고유번호로 정보조회
    public ProfileDto findById(Long id) {
        return usersMapper.findById(id);
    }

    public PhotoDto getMyPhoto(Long id) {
        return usersMapper.getMyPhoto(id);
    }

    // 회원 정보 수정
    public int updateUser(Users users) {
        if(usersMapper.findByUsernameNotMe(users).isPresent()) {
            throw new DuplicateUserException("이미 사용중인 아이디입니다.");
        }
        if(users.getPassword() != null) {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
        }
        return usersMapper.updateUser(users);
    }

    // 회원 정보 삭제
    public int deleteUser(Long id, Users users) {
        Users user = usersMapper.findByUsername(users.getUsername())
                .orElseThrow();

        if (!passwordEncoder.matches(users.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return usersMapper.deleteUser(id);
    }

    // username으로 사용자 조회
    public Optional<Users> findByUsername(String username) {
        return usersMapper.findByUsername(username);
    }

    // 이메일로 사용자 조회
    public Optional<Users> findByEmail(String email) {
        return usersMapper.findByEmail(email);
    }

    // 사용자 검색
    public List<Users>  searchByUsername(String keyword) {
        return usersMapper.searchByUsername(keyword);
    }
}
