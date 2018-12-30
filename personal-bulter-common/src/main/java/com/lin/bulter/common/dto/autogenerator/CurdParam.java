package com.lin.bulter.common.dto.autogenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurdParam {
    private boolean vue;            // 是否生成vue前端页面
    private boolean controller;     // 是否生成controller层
    private boolean service;        // 是否生成service层
    private boolean mapper;         // 是否生成dao层
    private boolean xml;            // 是否生成mybaits xml
    private String packageName;     // 包名字

}
