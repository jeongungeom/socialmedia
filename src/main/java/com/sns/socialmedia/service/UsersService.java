package com.sns.socialmedia.service;


import com.sns.socialmedia.Dto.PhotoDto;
import com.sns.socialmedia.Dto.ProfileDto;
import com.sns.socialmedia.Dto.TotalPhotoDto;
import com.sns.socialmedia.exception.DuplicateUserException;
import com.sns.socialmedia.mapper.CommentsMapper;
import com.sns.socialmedia.mapper.UsersMapper;
import com.sns.socialmedia.model.Comments;
import com.sns.socialmedia.model.Users;
import com.sns.socialmedia.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    private final UsersMapper usersMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${profile.upload.dir}")
    private String uploadDir;
    @Autowired
    private CommentsMapper commentsMapper;

    // 회원가입 및 아이디 중복 체크
    public void checkValid(Users users) {
        if(usersMapper.findByUsername(users.getUsername()).isPresent()) {
            throw new DuplicateUserException("이미 사용중인 아이디입니다.");
        } else if (usersMapper.findByEmail(users.getEmail()).isPresent()) {
            throw new DuplicateUserException("이미 사용중인 이메일입니다.");
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setProfilePicture("default_profile.jpg");

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

    // 게시물 단건 조회
    public TotalPhotoDto getMyPhoto(Long userId, Long id, Long myId , boolean isMyId) {
        TotalPhotoDto totalPhotoDto = new TotalPhotoDto();

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", id);
        params.put("myId", myId);
        params.put("isMyId", isMyId);
        PhotoDto photoDto = usersMapper.getMyPhoto(params);
        List<Comments> commentsList = commentsMapper.findMyPhotoId(id);

        totalPhotoDto.setPhotoDto(photoDto);
        totalPhotoDto.setCommentsList(commentsList);
        return totalPhotoDto;
    }

    // 게시물 다건 조회
    public List<PhotoDto> getMyPhotos(Long userId) {
        return usersMapper.getMyPhotos(userId);
    }

    // 회원 정보 수정
    @Transactional
    public String updateUser(Users users, MultipartFile profilePicture) {
        if(usersMapper.findByUsernameNotMe(users).isPresent()) {
            throw new DuplicateUserException("이미 사용중인 아이디입니다.");
        }
        if(users.getPassword() != null) {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
        }

        String profilePicFileName = null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String ext = Objects.requireNonNull(profilePicture.getOriginalFilename())
                    .substring(profilePicture.getOriginalFilename().lastIndexOf("."));
            profilePicFileName = "user_" + users.getId() + "_" + System.currentTimeMillis() + ext;
            Path savePath = Paths.get(uploadDir, profilePicFileName);
            users.setProfilePicture(profilePicFileName);
            try {
                Files.copy(profilePicture.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("프로필 사진 저장 실패", e);
            }
        }
        usersMapper.updateUser(users);

        return profilePicFileName;
    }

    public Resource loadProfileImage(String filename) {
        try {
            Path file = Paths.get(uploadDir, filename);
            if (Files.exists(file)) {
                return new UrlResource(file.toUri());
            }
            Resource defaultImage = new ClassPathResource("static/images/" + filename);
            if (defaultImage.exists()) {
                return defaultImage;
            }
            return null;
        } catch (MalformedURLException e) {
            return null;
        }
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
    public List<Users> searchByUsername(Long id, String keyword) {
        return usersMapper.searchByUsername(id, keyword);
    }

    public String getUsernameById(Long id) {
        return usersMapper.getUsernameById(id);
    }
}
