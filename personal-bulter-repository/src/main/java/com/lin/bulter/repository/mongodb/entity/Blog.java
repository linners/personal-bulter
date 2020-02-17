package com.lin.bulter.repository.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "blog")
public class Blog {
    private String id;
    private String title;
    private String content;
    private String creator;
    private Integer c_t;
    private Integer u_t;
}
