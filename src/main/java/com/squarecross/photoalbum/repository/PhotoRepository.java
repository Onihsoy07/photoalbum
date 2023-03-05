package com.squarecross.photoalbum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.squarecross.photoalbum.domain.*;
import java.util.*;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    int countByAlbum_AlbumId(Long AlbumId);

    List<Photo> findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(Long AlbumId);

    List<Photo> findByAlbum_AlbumId(Long AlbumId);

    Optional<Photo> findByFileNameAndAlbum_AlbumId(String fileName, Long albumId);

    List<Photo> findByAlbum_AlbumIdOrderByFileNameAsc(Long albumId);

    List<Photo> findByAlbum_AlbumIdOrderByFileNameDesc(Long albumId);

    List<Photo> findByAlbum_AlbumIdOrderByUploadedAtAsc(Long albumId);

    List<Photo> findByAlbum_AlbumIdOrderByUploadedAtDesc(Long albumId);

}
