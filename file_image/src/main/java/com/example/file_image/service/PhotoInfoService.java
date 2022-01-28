package com.example.file_image.service;

import com.example.file_image.entity.PhotoInfo;
import com.example.file_image.enums.Quality;
import com.example.file_image.payload.ResFileInfo;
import com.example.file_image.payload.Response;
import com.example.file_image.repository.PhotoInfoRepository;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @className: PhotoInfoSevice  $
 * @description: TODO
 * @date: 07 December 2021 $
 * @time: 1:26 AM $
 * @author: Qudratjon Komilov
 */
@Service
@Slf4j
public class PhotoInfoService {

    @Autowired
    PhotoInfoRepository photoInfoRepository;

    @Value("${file.folder}")
    private String path;

    public HttpEntity<Response> uploadFile(MultipartHttpServletRequest request) {
        Iterator<String> iterator = request.getFileNames();
        MultipartFile multipartFile;
        List<ResFileInfo> resFileInfos = new ArrayList<>();
        while (iterator.hasNext()) {
            multipartFile = request.getFile(iterator.next());
            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.setSize(multipartFile.getSize());
            photoInfo.setName(multipartFile.getOriginalFilename());
            photoInfo.setContentType(multipartFile.getContentType());
            photoInfo.setExtension(getExtension(multipartFile.getOriginalFilename()));
            try {
                PhotoInfo save = photoInfoRepository.save(photoInfo);
                Calendar calendar = new GregorianCalendar();
                File uploadFolder = new File(path + "/" + Quality.ORIGINAL + "/" + calendar.get(Calendar.YEAR) + "/" +
                        (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH));
                if (uploadFolder.mkdirs() && uploadFolder.exists()) {
                    log.info("create new folder ==> " + uploadFolder.getAbsolutePath());
                }
                File file = new File(uploadFolder + "/" + save.getId() + "_" + save.getName());
                save.setPathOriginal(file.getAbsolutePath());
                multipartFile.transferTo(file);
                save.setPathAverage(resize(file.getAbsolutePath(), Quality.AVERAGE, 0.8f));
                save.setPathLow(resize(file.getAbsolutePath(), Quality.LOW, 0.4f));
                photoInfoRepository.save(save);
                resFileInfos.add(new ResFileInfo(save.getId(), save.getName(), save.getContentType(), save.getSize()));
            } catch (IOException e) {
                log.error("error", e.getMessage());
            }

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response("save", HttpStatus.ACCEPTED.value(), resFileInfos));

    }

    public String resize(String pathFile, Quality qualityType, float quality) {
        try {
            File file = new File(pathFile);
            BufferedImage read = ImageIO.read(file);
            Calendar calendar = new GregorianCalendar();
            String fileP = path + "/" + qualityType + "/" + calendar.get(Calendar.YEAR) + "/" +
                    (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
            File file1 = new File(fileP + read.getWidth() + "_" + read.getHeight() + "_" + quality + file.getName());

            if (file.exists() && file1.isFile()) {
                return file1.getAbsolutePath();
            }
            File uploadFolder = new File(fileP);
            if (uploadFolder.mkdirs() && uploadFolder.exists()) {
                log.info("create new folder ==> " + uploadFolder.getAbsolutePath());
            }
            BufferedImage bufferedImage = resizeImage(ImageIO.read(file), read.getWidth(), read.getHeight(), quality);
            ImageIO.write(bufferedImage, "jpg", file1);
            return file1.getAbsolutePath();
        } catch (Exception e) {
            return pathFile;
        }
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight, float quality) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(quality)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }

    public String getExtension(String fileName) {
        String e = null;
        if (fileName != null && !fileName.isEmpty()) {
            int point = fileName.lastIndexOf(".");
            if (point > 0 && point <= fileName.length() - 2) {
                e = fileName.substring(point);
            }
        }
        return e;
    }

    public HttpEntity<?> downloadFile(Long id, Quality quality) throws IOException {
        try {
            PhotoInfo photo = photoInfoRepository.findById(id).orElseThrow(() -> new ResolutionException("phohto id"));
            return switch (quality) {
                case LOW -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photo.getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename\"" + photo.getName())
                        .body(Files.readAllBytes(Path.of(photo.getPathLow())));
                case AVERAGE -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photo.getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename\"" + photo.getName())
                        .body(Files.readAllBytes(Path.of(photo.getPathAverage())));
                default -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photo.getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename\"" + photo.getName())
                        .body(Files.readAllBytes(Path.of(photo.getPathOriginal())));

            };
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(StreamUtils.copyToByteArray(new ClassPathResource("static/fox_familyr.jpg").getInputStream()));
        }
    }

}











