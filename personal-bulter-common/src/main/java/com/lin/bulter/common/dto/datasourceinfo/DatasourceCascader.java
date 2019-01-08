package com.lin.bulter.common.dto.datasourceinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasourceCascader {
    private String value;           // 后台传的值
    private String label;           // 显示的值
    private List<DatasourceCascader> children;
}
