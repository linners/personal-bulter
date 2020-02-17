package com.lin.bulter.repository.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "music")
public class Music {
    private String id;
    private Integer music_id;
    private String music_title;
    private String music_artist;
    private String music_src;
    private String music_pic;
    private Integer c_t;
    private Integer u_t;
}
