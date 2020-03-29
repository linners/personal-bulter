package com.lin.bulter.web.controller;

import com.lin.bulter.common.utils.FileUploadUtils;
import com.lin.bulter.common.utils.ReflectionsUtil;
import com.lin.bulter.repository.mongodb.entity.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/bulter/file")
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    private static String uploadPath = "E:\\testjar\\";

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String fileName = file.getOriginalFilename();
        File dest = new File(uploadPath + fileName);
        try {
            file.transferTo(dest);
            logger.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        // 初始化reflection组件
        try {
            ReflectionsUtil.init(uploadPath, "com.jd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }

}
