package com.example.file_image.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @className: ResFileInfo  $
 * @description: TODO
 * @date: 07 December 2021 $
 * @time: 3:55 AM $
 * @author: Qudratjon Komilov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResFileInfo {

    private Long fileId;
    private String fileName;
    private String fileType;
    private Long size;

}
