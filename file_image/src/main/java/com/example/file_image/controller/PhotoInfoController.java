package com.example.file_image.controller;

import com.example.file_image.enums.Quality;
import com.example.file_image.payload.Response;
import com.example.file_image.service.PhotoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

/**
 * @className: PhotoInfoController  $
 * @description: TODO
 * @date: 07 December 2021 $
 * @time: 1:27 AM $
 * @author: Qudratjon Komilov
 */
@Slf4j
@RestController
@RequestMapping("/koinot/file")
public class PhotoInfoController {
    @Autowired
    PhotoInfoService photoInfoService;

    @PostMapping("/uploadFile")
    public HttpEntity<Response> uploadFile(MultipartHttpServletRequest request) {
        return photoInfoService.uploadFile(request);
    }

    @GetMapping("/downloadFile/{id}")
    public HttpEntity<?> downloadFile(@PathVariable Long id, @RequestParam(required = false, defaultValue = "ORIGINAL") Quality quality) throws IOException {
        return photoInfoService.downloadFile(id, quality);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public HttpEntity<?> catchException(Exception e) {
        log.error("file controller ", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

}
