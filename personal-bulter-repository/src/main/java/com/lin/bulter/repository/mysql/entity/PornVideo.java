package com.lin.bulter.repository.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PornVideo {
    
    private Integer id; 
    
    private String video; 
    
    private String title; 
    
    private String img; 
    // 状态, 0: 正常  1: 最爱 
    private Integer status; 
    
}
