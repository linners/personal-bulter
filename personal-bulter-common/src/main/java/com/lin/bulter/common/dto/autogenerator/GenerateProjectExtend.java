package com.lin.bulter.common.dto.autogenerator;

import com.lin.bulter.common.utils.VelocityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.velocity.VelocityContext;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateProjectExtend extends GenerateParam{

    // git模板工程全路径
    private String gitProjectPath;
    // velocityUtils实例
    private VelocityUtils instance;
    // velocityContext参数
    private VelocityContext context;
    // 新生成的工程存放目录
    private String generatedProjectPath;
}
