package com.lin.bulter.common.dto.datasourceinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasourceDto {

    private Integer datasourceId;                   // 数据源Id
    private String datasourceName;                  // 数据源清楚
    private List<DatabaseInfo> databaseInfos;       // 数据源下的所有数据库
}
