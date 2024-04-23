package cn.itsource.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface OosService {
    String upload(MultipartFile file) throws IOException;
}
