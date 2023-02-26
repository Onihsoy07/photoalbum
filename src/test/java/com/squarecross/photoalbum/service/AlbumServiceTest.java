package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.mapper.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.squarecross.photoalbum.repository.*;
import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

//    @Test
//    void testPhotoCount() {
//        Album album = new Album();
//        album.setAlbumName("테스트");
//        Album saveAlbum = albumRepository.save(album);
//
//        Photo photo1 = new Photo();
//        photo1.setFileName("사진1");
//        photo1.setAlbum(saveAlbum);
//        photoRepository.save(photo1);
//
//        Photo photo2 = new Photo();
//        photo2.setFileName("사진2");
//        photo2.setAlbum(saveAlbum);
//        Photo savePhoto = photoRepository.save(photo2);
//
//        AlbumDto albumDto = albumService.getAlbum(saveAlbum.getAlbumId());
//        assertEquals(2, albumDto.getCount());
//
//    }

//    @Test
//    void testAlbumCreate() throws IOException {
//        AlbumDto albumDto = new AlbumDto();
//        albumDto.setAlbumName("테스트 앨범");
//        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
//        File originalFile = new File(String.format(Constants.PATH_PREFIX + "/photos/original/" + savedAlbumDto.getAlbumId()));
//        File thumbFile = new File(String.format(Constants.PATH_PREFIX + "/photos/thumb/" + savedAlbumDto.getAlbumId()));
//
//        assertThat(originalFile).exists();
//        assertThat(thumbFile).exists();
//
//        albumService.deletAlbum(savedAlbumDto);
//
//        assertTrue(!(originalFile.isDirectory()));
//        assertTrue(!(thumbFile.isDirectory()));
//
//    }

    @Test
    void testAlbumRepository() throws InterruptedException {
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaaa");
        album2.setAlbumName("aaab");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        assertEquals("aaab", resDate.get(0).getAlbumName());
        assertEquals("aaaa", resDate.get(1).getAlbumName());
        assertEquals(2, resDate.size());

        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        assertEquals("aaab", resDate.get(0).getAlbumName());
        assertEquals("aaaa", resDate.get(1).getAlbumName());
        assertEquals(2, resDate.size());

    }


}