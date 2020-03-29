package com.lin.bulter.common.param;

import com.lin.bulter.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassJsonParam {

    private PageParam page;

    private Integer id;

    private String className;

    private String methodName;

    private String argClassList;

    private String jsonVal;

}
