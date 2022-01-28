package com.example.file_image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileImageApplication {


    public static void main(String[] args) {
        SpringApplication.run(FileImageApplication.class, args);
    }


}
