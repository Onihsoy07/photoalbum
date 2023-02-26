package com.squarecross.photoalbum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.squarecross.photoalbum.domain.*;
import com.squarecross.photoalbum.repository.*;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.nio.file.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    private final PhotoRepository photoRepository;

    public AlbumDto getAlbum(Long albumId) {
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다.", albumId));
        }
    }

    public AlbumDto getAlbum(String albumName) {
        Optional<Album> res = albumRepository.findByAlbumName(albumName);
        if(res.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumDto.getAlbumId()));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범명 %s로 조회되지 않았습니다.", albumName));
        }
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        this.albumRepository.save(album);
        this.createAlbumDirectories(album);
        return AlbumMapper.convertToDto(album);
    }

    public void deletAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        deleteAlbumDirectories(album);
    }

    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    private void deleteAlbumDirectories(Album album) throws IOException {
        Path originalPath = Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId());
        Path thumbPath = Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId());
        Boolean isFile = Files.isDirectory(originalPath) && Files.isDirectory(thumbPath);
        if(isFile) {
            Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
            Files.delete(Paths.get(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
            System.out.println("삭제 되었습니다.");
        } else {
            System.out.printf("%s\n%s  의 폴더가 존재하지 않습니다.\n", originalPath.toString(), thumbPath.toString());
        }
    }

    public List<AlbumDto> getAlbumList(String keyword, String sort, String orderBy) {
        List<Album> albums;
        if("byName".equals(sort)) {
            if("desc".equals(orderBy)) {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameDesc(keyword);
            } else if("asc".equals(orderBy)) {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
            } else {
                throw new IllegalArgumentException("알 수 없는 정렬 기준입니다.");
            }
        } else if("byDate".equals(sort)) {
            if("desc".equals(orderBy)) {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameDesc(keyword);
            } else if("asc".equals(orderBy)) {
                albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
            } else {
                throw new IllegalArgumentException("알 수 없는 정렬 기준입니다.");
            }
        } else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다.");
        }
        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);

        for(AlbumDto albumDto : albumDtos) {
            List<Photo> top4 = photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream().map(Photo::getThumbUrl).map(c -> Constants.PATH_PREFIX + c).collect(Collectors.toList()));
        }
        return albumDtos;
    }

}
