package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import com.squarecross.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @GetMapping("/download")
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse response) {
        try {
            if(photoIds.length == 1) {
                File file = photoService.getImageFile(photoIds[0]);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(new FileInputStream(file), outputStream);
                outputStream.close();
            } else {
                ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
                for(Long photoId : photoIds) {
                    File file = photoService.getImageFile(photoId);
                    zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                    FileInputStream fileInputStream = new FileInputStream(file);

                    StreamUtils.copy(fileInputStream, zipOutputStream);

                    fileInputStream.close();
                    zipOutputStream.closeEntry();
                }
                zipOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
