package cn.itsource.controller.app;

import cn.itsource.result.R;
import cn.itsource.service.OosService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/app/upload")
public class OosController {
    @Resource
    private OosService oosService;

    @PostMapping("/oos")
    public R<String> upload(MultipartFile file) throws IOException {
        String filePath = oosService.upload(file);
        return R.success(filePath);
    }
}
