package com.lin.bulter.web.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/29 23:11
 */
@Data
@NoArgsConstructor
public class MockJsonVo {

	private String className;
	private String methodName;
	private String[] argClassNameList;
	private String json;
}
