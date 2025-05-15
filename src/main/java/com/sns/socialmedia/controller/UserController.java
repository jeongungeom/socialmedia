package com.sns.socialmedia.controller;

import com.sns.socialmedia.Dto.PhotoDto;
import com.sns.socialmedia.Dto.ProfileDto;
import com.sns.socialmedia.model.Users;
import com.sns.socialmedia.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UsersService usersService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users users) {
        log.debug("regsiter");
        usersService.checkValid(users);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인 (JWT 발급)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        String token = usersService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // 회원정보 조회
    @GetMapping("/profile")
    public ProfileDto getProfile(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return usersService.findById(id);
    }

    // 게시물 조회
    @GetMapping("/photo")
    public PhotoDto getPhoto(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return usersService.getMyPhoto(id);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @RequestBody Users updateUser) {
        updateUser.setId((Long) request.getAttribute("id"));
        usersService.updateUser(updateUser);
        return ResponseEntity.ok("프로필이 수정되었습니다.");
    }

    @PutMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @RequestBody Users deleteUser) {
        Long id = (Long) request.getAttribute("id");
        usersService.deleteUser(id, deleteUser);
        return ResponseEntity.ok("계정이 삭제되었습니다.");
    }




}
