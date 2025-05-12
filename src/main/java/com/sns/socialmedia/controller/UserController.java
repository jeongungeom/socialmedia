package com.sns.socialmedia.controller;

import com.sns.socialmedia.model.Users;
import com.sns.socialmedia.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UsersService usersService;


    @GetMapping("/csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        return Map.of("token", csrfToken.getToken());
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users users) {
        log.debug("regsiter");
        usersService.checkValid(users);
        return ResponseEntity.ok("회원가입 성공");
    }
}
