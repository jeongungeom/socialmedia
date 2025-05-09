package com.sns.socialmedia;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class testClass {
    @GetMapping("/api/test")
    public String testApi() {
        return "Spring Boot 연결 성공!!!!!";
    }
}
