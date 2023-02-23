package com.squarecross.photoalbum.service;

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

    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album saveAlbum = albumRepository.save(album);

        Album resAlbum = albumService.getAlbum(saveAlbum.getAlbumId());
        assertEquals("TEST", resAlbum.getAlbumName());
    }

//    @Test
//    void getAlbum() {
//        Album album = new Album();
//        album.setAlbumName("테스트");
//        Album saveAlbum = albumRepository.save(album);
//
//        Album resAlbum = albumService.getAlbum(saveAlbum.getAlbumName());
//        assertEquals("테스트", resAlbum.getAlbumName());
//    }

}