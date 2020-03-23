package com.lin.bulter.web.vo;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/24 0:33
 */
public class MockContext {

	private String className;
	private String methodName;
	private String alias;
	private Object[] argClasses;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Object[] getArgClasses() {
		return argClasses;
	}

	public void setArgClasses(Object[] argClasses) {
		this.argClasses = argClasses;
	}
}
