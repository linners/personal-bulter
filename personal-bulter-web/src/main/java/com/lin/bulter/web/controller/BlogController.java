package com.lin.bulter.web.controller;

import com.lin.bulter.common.utils.FileUploadUtils;
import com.lin.bulter.repository.mongodb.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/bulter/blog")
public class BlogController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/findBlogs")
    public List<Blog> findUsers(){
        List<Blog> result = mongoTemplate.findAll(Blog.class);
        return result;
    }

    @PostMapping("/saveBlog")
    public Blog saveUser(@RequestBody Blog blog) throws Exception{
        Blog result = mongoTemplate.insert(blog);
        return result;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono uploadFile(@RequestPart("file") FilePart filePart) throws IOException {
        System.out.println("参数："+filePart.filename());
        Flux<DataBuffer> content = filePart.content();
        Path tempFile = Files.createTempFile("test", filePart.filename());
        File file1 = tempFile.toFile();
        filePart.transferTo(file1);
        String path = "";
        try {
            path = new FileUploadUtils().upload(file1, filePart.filename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.just(path);
    }
}
