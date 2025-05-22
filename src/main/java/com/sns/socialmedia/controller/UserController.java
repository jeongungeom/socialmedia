package com.sns.socialmedia.controller;

import com.sns.socialmedia.Dto.PhotoDto;
import com.sns.socialmedia.Dto.ProfileDto;
import com.sns.socialmedia.Dto.TotalPhotoDto;
import com.sns.socialmedia.model.Users;
import com.sns.socialmedia.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    // 내정보 조회
    @GetMapping("/profile")
    public ProfileDto getProfile(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return usersService.findById(id);
    }

    // 상대방 페이지 조회
    @GetMapping("/profile/{id}")
    public ProfileDto getOtherProfile(@PathVariable("id") Long id) {
        return usersService.findById(id);
    }

//    // 게시물 단건 조회
//    @GetMapping("/photo")
//    public PhotoDto getPhoto(HttpServletRequest request, Long id) {
//        Long userId = (Long) request.getAttribute("id");
//        return usersService.getMyPhoto(userId, id);
//    }

    // 게시물 다건 조회
    @GetMapping("/photos")
    public List<PhotoDto> getPhotos(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("id");
        return usersService.getMyPhotos(userId);
    }
//
//    // 상대방 게시물 단건 조회
//    @GetMapping("/photo/{id}")
//    public PhotoDto getOtherPhoto(@PathVariable("id") Long userId, Long id) {
//        return usersService.getMyPhoto(userId, id);
//    }

    // 상대방 다건 게시물 조회
    @GetMapping("/photos/{id}")
    public List<PhotoDto> getOtherPhotos(@PathVariable("id") Long id) {
        return usersService.getMyPhotos(id);
    }

    // 게시물 단건 조회 통합
    @GetMapping("/photoOne")
    public TotalPhotoDto getPhotoOne(HttpServletRequest request, @RequestParam("userId") Long userId, @RequestParam("id") Long id) {
        Long myId = (Long) request.getAttribute("id");
        boolean isMyId = myId.equals(userId);
        return usersService.getMyPhoto(userId, id, myId, isMyId);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestParam("username") String username,
            @RequestParam("bio") String bio,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            HttpServletRequest request
    ) {
        Users users = new Users((Long) request.getAttribute("id"), username, email, password, bio);

        String imgUrl =  usersService.updateUser(users, profilePicture);
        return ResponseEntity.ok(imgUrl);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) {
        Resource file = usersService.loadProfileImage(filename);
        if (file == null || !file.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = "image/jpeg";
        String ext = StringUtils.getFilenameExtension(filename);
        if ("png".equalsIgnoreCase(ext)) contentType = "image/png";
        else if ("gif".equalsIgnoreCase(ext)) contentType = "image/gif";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }

    @PutMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, @RequestBody Users deleteUser) {
        Long id = (Long) request.getAttribute("id");
        usersService.deleteUser(id, deleteUser);
        return ResponseEntity.ok("계정이 삭제되었습니다.");
    }

    @GetMapping("/searchUser")
    public ResponseEntity<?> searchUser(HttpServletRequest request, @RequestParam("keyword") String keyword) {
        Long id = (Long) request.getAttribute("id");
        List<Users> usersList = usersService.searchByUsername(id, keyword);
        return ResponseEntity.ok(usersList);
    }




}
