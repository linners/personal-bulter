package com.lin.bulter.repository.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "plan")
public class PublicLog {

    @Id
    private String id;
    private Integer task_type;
    private String task_name;
    private Integer task_level;
    private Integer task_classfiy;
    private String[] task_endtime;
    private String task_content;
    private Integer c_t = Math.toIntExact(System.currentTimeMillis() / 1000);
    private Integer u_t = Math.toIntExact(System.currentTimeMillis() / 1000);
}
