package com.lin.bulter.repository.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassJson {
    
    private Integer id; 
    
    private String className; 
    
    private String methodName; 
    
    private String argClassList; 
    
    private String jsonVal; 
    
}
