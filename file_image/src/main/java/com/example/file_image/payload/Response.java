package com.example.file_image.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @className: Response  $
 * @description: TODO
 * @date: 07 December 2021 $
 * @time: 1:30 AM $
 * @author: Qudratjon Komilov
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String message;
    private int success;
    private Object info;

    public Response(String message, int success) {
        this.message = message;
        this.success = success;
    }


}
