package com.sns.socialmedia.controller;

import com.sns.socialmedia.Dto.FeedDto;
import com.sns.socialmedia.model.Photos;
import com.sns.socialmedia.service.PhotoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("caption") String caption,  HttpServletRequest request) {
        String uploadDir = "C:\\File\\Post\\";
        Long userId = (Long) request.getAttribute("id");
        String fileName = file.getOriginalFilename();
        try {
            file.transferTo(new File(uploadDir + fileName));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("파일 업로드 실패: " + fileName);
        }
        String imgUrl = "/uploads/"+fileName;
        Photos photos = new Photos(userId, imgUrl, caption);

        photoService.insertPhotos(photos);

        return ResponseEntity.ok("업로드 성공: " + fileName);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePhoto(HttpServletRequest request, @PathVariable("id") Long id) {
        Long userId = (Long) request.getAttribute("id");
        photoService.deletePhotos(userId, id);
    }

    @GetMapping("/allPhotos")
    public List<FeedDto> getAllPhotos(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return photoService.getAllPhotos(id);
    }

    @PutMapping("/update/{id}")
    public void update(@RequestBody Photos photos, @PathVariable("id") Long id) {
        photos.setId(id);
        photoService.updatePhotos(photos);
    }
}
