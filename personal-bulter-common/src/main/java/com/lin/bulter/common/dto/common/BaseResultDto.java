package com.lin.bulter.common.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResultDto<T> implements Serializable {

    //是否成功
    private boolean success;

    // 返回消息的错误编码
    private Integer ret;

    // 返回消息
    private String resultMessage;

    // 返回的对象
    private T data;

    public static <T> BaseResultDto<T> renderSuccessResult(T data) {
        BaseResultDto<T> baseResultDto = new BaseResultDto<T>();
        baseResultDto.setRet(1);
        baseResultDto.setSuccess(true);
        baseResultDto.setData(data);
        return baseResultDto;
    }

    public static <T> BaseResultDto<T> renderFailResult(String resultMessage) {
        BaseResultDto<T> baseResultDto = new BaseResultDto<T>();
        baseResultDto.setRet(0);
        baseResultDto.setSuccess(false);
        baseResultDto.setResultMessage(resultMessage);
        return baseResultDto;
    }
}
