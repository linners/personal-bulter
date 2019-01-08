package com.lin.bulter.common.dto.autogenerator;

import com.lin.bulter.common.dto.datasourceinfo.TableColumnInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurdEntityInfo {

    private String entityName;                  // entity名称
    private String tableName;                   // 数据库的名字
    private List<TableColumnInfo> tableColumns;     // 数据库字段信息

}
