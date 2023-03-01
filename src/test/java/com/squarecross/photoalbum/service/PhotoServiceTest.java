package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoServiceTest {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoService photoService;

    @Test
    void testGetPhoto() {
        Album album = new Album();
        album.setAlbumName("test");
        Album savedAlbum = albumRepository.save(album);

        Photo photo = new Photo();
        photo.setFileName("test1");
        photo.setAlbum(savedAlbum);
        Photo savedPhoto = photoRepository.save(photo);

        PhotoDto photoDto = photoService.getPhoto(savedPhoto.getPhotoId());

        assertEquals("test1", photoDto.getFileName());

    }


}