package cn.itsource.service.impl;

import cn.itsource.exception.GlobalException;
import cn.itsource.service.OosService;
import cn.itsource.template.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class OosServiceImpl implements OosService {

    @Resource
    private OssTemplate ossTemplate;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String imgUrl;
        if (!file.isEmpty()) {
            imgUrl = ossTemplate.getImgUrl(file);
        }else {
            throw new GlobalException("文件地址获取失败！");
        }
        return imgUrl;
    }
}
