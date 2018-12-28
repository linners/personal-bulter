package com.lin.bulter.common.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam<T> implements Serializable {

    private Integer pageSize;
    private Integer pageNum;

    private List<T> list;
    private int pages;
    private long total;

    public PageParam(List<T> list, long total) {
        this.total = total;
        this.list = list;
    }

    public PageParam(List<T> list, long total, int pages) {
        this.total = total;
        this.list = list;
        this.pages = pages;
    }
}
