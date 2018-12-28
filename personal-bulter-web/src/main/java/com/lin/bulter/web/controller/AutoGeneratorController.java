package com.lin.bulter.web.controller;

import com.lin.bulter.business.autogenerator.GeneratorService;
import com.lin.bulter.common.dto.autogenerator.GenerateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bulter/generate")
public class AutoGeneratorController {

    private static Logger logger = LoggerFactory.getLogger(AutoGeneratorController.class);

    @Value("${generate.downDomain}")
    private String downDomain;

    @Autowired
    private GeneratorService generatorService;

    @PostMapping("/project")
    public String generateProject(@RequestBody GenerateParam param) {
        String zipPath = generatorService.generatorProject(param);
        String download = downDomain + "/download/" + zipPath;
        logger.info(">>>>>>>>自动生成结束, 下载地址: {}", download);
        return download;
    }
}
