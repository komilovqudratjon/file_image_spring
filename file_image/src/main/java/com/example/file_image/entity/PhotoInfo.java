package com.example.file_image.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @className: PhotoInfo  $
 * @description: TODO
 * @date: 07 December 2021 $
 * @time: 1:17 AM $
 * @author: Qudratjon Komilov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "photo_info")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name","size","contentType","pathOriginal"})})
public class PhotoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    private Timestamp updateAt;

    private String name;

    private String contentType;

    private String extension;

    private String pathOriginal;

    private String pathAverage;

    private String pathLow;

    private long size;


}
