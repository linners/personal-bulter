package com.lin.bulter.web.controller;

import com.lin.bulter.common.utils.FileUploadUtils;
import com.lin.bulter.repository.mongodb.entity.Plan;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

/**
 * 小窝（计划）controller
 *
 * @author wangchenglin13
 * @date 2020-02-17 11:23
 */
@RestController
@RequestMapping("/bulter/plan")
public class PlanController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/findTasks")
    public List<Plan> findTasks(){
        List<Plan> result = mongoTemplate.findAll(Plan.class);
        return result;
    }

    @PostMapping("/addTask")
    public Plan addTask(@RequestBody Plan plan){
        Plan result = mongoTemplate.save(plan);
        return result;
    }

    @PostMapping("/updateTask")
    public Long updateTask(@RequestBody Plan plan){
        Query query = Query.query(Criteria.where("id").is(plan.getId()));
        Update update = new Update();
        update.set("task_type", plan.getTask_type());
        update.set("u_t", Math.toIntExact(System.currentTimeMillis() / 1000));
        UpdateResult result = mongoTemplate.updateFirst(query, update, Plan.class);
        return result.getMatchedCount();
    }

    @PostMapping("/delTask")
    public Long delTask(@RequestBody Plan plan){
        Plan planOld = mongoTemplate.findById(plan.getId(), Plan.class);
        DeleteResult result = mongoTemplate.remove(planOld);
        return result.getDeletedCount();
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
