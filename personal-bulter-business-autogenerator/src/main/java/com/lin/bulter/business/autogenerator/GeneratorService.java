package com.lin.bulter.business.autogenerator;

import com.lin.bulter.common.dto.autogenerator.CurdParam;
import com.lin.bulter.common.dto.autogenerator.GenerateParam;

public interface GeneratorService {

    /**
     * 自动生成工程框架
     * @return
     */
    String generatorProject(GenerateParam param);

    /**
     * 自动生成增删改查
     * @param param
     * @return
     */
    String generatorCrud(CurdParam param);
}
