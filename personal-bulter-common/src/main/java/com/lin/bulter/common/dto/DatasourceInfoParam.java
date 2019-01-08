package com.lin.bulter.common.dto;

import com.lin.bulter.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasourceInfoParam {

    private PageParam page;
    
    private Integer id; 
    // 数据源名称 
    private String datasourceName; 
    // 数据源ip 
    private String dbIp; 
    // 数据源端口号 
    private Integer dbPort; 
    // 用户名 
    private String userName; 
    // 用户名密码 
    private String password;

}
