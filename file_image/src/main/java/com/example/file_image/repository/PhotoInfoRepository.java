package com.example.file_image.repository;

import com.example.file_image.entity.PhotoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @className: PhotoInfoRepository  $
 * @description: TODO
 * @date: 07 December 2021 $
 * @time: 1:26 AM $
 * @author: Qudratjon Komilov
 */
@Repository
public interface PhotoInfoRepository extends JpaRepository<PhotoInfo,Long> {
}
