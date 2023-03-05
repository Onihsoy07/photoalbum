package com.squarecross.photoalbum.service;

import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.springframework.util.StringUtils;
import com.squarecross.photoalbum.dto.*;
import com.squarecross.photoalbum.mapper.*;
import com.squarecross.photoalbum.repository.*;
import com.squarecross.photoalbum.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    private final AlbumRepository albumRepository;

    private final String original_path = Constants.PATH_PREFIX + "/photos/original";
    private final String thumb_path = Constants.PATH_PREFIX + "/photos/thumb";

    public PhotoDto getPhoto(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isEmpty()) { throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회되지 않았습니다.", photoId)); }

        PhotoDto photoDto = PhotoMapper.convertToDto(res.get());
        return photoDto;
    }

    public List<PhotoDto> getAlbumPhotos(Long albumId) {
        return PhotoMapper.convertToDtoList(photoRepository.findByAlbum_AlbumId(albumId));
    }

    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        Optional<Album> res = albumRepository.findById(albumId);
        if(res.isEmpty())   { throw new EntityNotFoundException("앨범이 존재하지 않습니다."); }

        if(!(checkImageMimeType(file))) { throw new IllegalArgumentException(String.format("%s 파일은 이미지 파일이 아닙니다.", file.getOriginalFilename())); };

        String fileName = file.getOriginalFilename();
        int fileSize = (int)file.getSize();
        fileName = getNextFileName(fileName, albumId);
        saveFile(file, albumId, fileName);

        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName);
        photo.setFileSize(fileSize);
        photo.setAlbum(res.get());
        Photo createdPhoto = photoRepository.save(photo);

        return PhotoMapper.convertToDto(createdPhoto);
    }

    private String getNextFileName(String fileName, Long albumId) {
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName);
        String ext = StringUtils.getFilenameExtension(fileName);

        Optional<Photo> res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);

        int count = 2;
        while(res.isPresent()) {
            fileName = String.format("%s (%d).%s", fileNameNoExt, count, ext);
            res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
            count++;
        }

        return fileName;
    }

    private void saveFile(MultipartFile file, Long albumId, String fileName) throws IOException {
        try {
            String filePath = albumId + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(original_path + "/" + filePath));

            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);
            File thumbFile = new File(thumb_path + "/" + filePath);
            String ext = StringUtils.getFilenameExtension(fileName);
            if (ext == null) {
                throw new IllegalArgumentException("No Extention");
            }

            ImageIO.write(thumbImg, ext, thumbFile);
        } catch (Exception e) {
            throw new RemoteException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Boolean checkImageMimeType(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());
        Boolean check = mimeType.startsWith("image");
        return check ? true : false;
    }

    public File getImageFile(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId);
        if(res.isEmpty())   { throw new EntityNotFoundException(String.format("사진 ID %d를 찾을 수 없습니다.", photoId)); }

        return new File(Constants.PATH_PREFIX + res.get().getOriginalUrl());
    }


}
