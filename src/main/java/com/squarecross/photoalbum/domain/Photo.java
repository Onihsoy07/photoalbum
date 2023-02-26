package com.squarecross.photoalbum.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "photo", schema = "photo_album", uniqueConstraints = @UniqueConstraint(columnNames = "photo_id"))
public class Photo {

    public Photo() {};

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long photoId;

    @Column(name = "file_name", unique = false, nullable = true)
    private String fileName;

    @Column(name = "thumb_url", unique = false, nullable = true)
    private String thumbUrl;

    @Column(name = "original_url", unique = false, nullable = true)
    private String originalUrl;

    @Column(name = "file_size", unique = false, nullable = true)
    private Long fileSize;

    @Column(name = "uploaded_at", unique = false, nullable = true)
    @CreationTimestamp
    private Date uploadedAt;

}
