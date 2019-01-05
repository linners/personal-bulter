package com.lin.bulter.common.dto.autogenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateParam {
    // git地址
    private String gitRepository;
    private String branchName;
    // git工程，包名
    private String oldBasePackage;
    // maven相关
    private String rootGroupId;
    private String rootArtifactId;
    private String version;
    // 新工程相关
    private String projectName;
    private String basePackage;
}
