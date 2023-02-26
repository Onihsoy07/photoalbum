package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") final long albumId) {
//        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(albumService.getAlbum(albumId), HttpStatus.OK);
//        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<AlbumDto> getAlbumByQuery(@RequestParam final Long albumId) {
//        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(albumService.getAlbum(albumId), HttpStatus.OK);
    }

    @PostMapping("/json_body")
    public ResponseEntity<AlbumDto> getAlbumByJson(@RequestBody final AlbumDto albumDto) {
        AlbumDto album = albumService.getAlbum(albumDto.getAlbumId());
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<AlbumDto>>
    getAlbumList(@RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword,
                 @RequestParam(value = "sort", required = false, defaultValue = "byDate") final String sort,
                 @RequestParam(value = "orderBy", required = false, defaultValue = "desc") final String orderBy) {
        List<AlbumDto> albumDtos = albumService.getAlbumList(keyword, sort, orderBy);
        return new ResponseEntity<>(albumDtos, HttpStatus.OK);
    }

}
