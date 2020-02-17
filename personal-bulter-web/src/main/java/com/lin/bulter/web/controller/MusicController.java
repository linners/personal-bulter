package com.lin.bulter.web.controller;

import com.lin.bulter.repository.mongodb.entity.Music;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bulter/music")
public class MusicController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/findMusics")
    public List<Music> findTasks(){
        List<Music> result = mongoTemplate.findAll(Music.class);
        return result;
    }

    @PostMapping("/addMusic")
    public Music addTask(@RequestBody Music music) throws Exception{
        Music result = mongoTemplate.insert(music);
        return result;
    }

    @PostMapping("/delMusic")
    public DeleteResult delMusic(@RequestBody Music music) throws Exception{
        DeleteResult result = mongoTemplate.remove(music);
        return result;
    }
}
