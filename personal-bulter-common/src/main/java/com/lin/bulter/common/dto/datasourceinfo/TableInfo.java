package com.lin.bulter.common.dto.datasourceinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {
    private String ownDbName;               // 所属的数据库名称
    private String tableName;               // 表名字
    private String tableComment;            // 表描述
    private List<TableColumnInfo> columns;      // 所有字段信息
}
