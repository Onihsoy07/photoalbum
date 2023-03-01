package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import com.squarecross.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable final Long photoId) {
        PhotoDto photoDto = photoService.getPhoto(photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<PhotoDto>> getAlbumPhotos(@PathVariable final Long albumId) {
        List<PhotoDto> photos = photoService.getAlbumPhotos(albumId);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable final Long albumId,
                                                       @RequestParam("photos")MultipartFile[] files) throws IOException {
        List<PhotoDto> photos = new ArrayList<>();
        for(MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
        }
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

}
