package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.mapper.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.squarecross.photoalbum.repository.*;
import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    AlbumService albumService;

    @Autowired
    PhotoRepository photoRepository;


//    @Test
//    void getAlbum() {
//        Album album = new Album();
//        album.setAlbumName("테스트");
//        Album saveAlbum = albumRepository.save(album);
//
//        AlbumDto resAlbum = albumService.getAlbum(saveAlbum.getAlbumId());
//        assertEquals("TEST", resAlbum.getAlbumName());
//    }

//    @Test
//    void getAlbum() {
//        Album album = new Album();
//        album.setAlbumName("테스트");
//        Album saveAlbum = albumRepository.save(album);
//
//        Album resAlbum = albumService.getAlbum(saveAlbum.getAlbumName());
//        assertEquals("테스트", resAlbum.getAlbumName());
//    }

    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album saveAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");
        photo1.setAlbum(saveAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("사진2");
        photo2.setAlbum(saveAlbum);
        Photo savePhoto = photoRepository.save(photo2);

        AlbumDto albumDto = albumService.getAlbum(saveAlbum.getAlbumId());
        assertEquals(2, albumDto.getCount());

    }

}