package com.squarecross.photoalbum.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class PhotoDto {
    private Long photoId;
    private String fileName;
    private int fileSize;
    private String originalUrl;
    private String thumbUrl;
    private Date uploadedAt;
    private Long albumId;
}
