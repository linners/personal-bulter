package com.lin.bulter.common.dto;

import com.lin.bulter.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleParam {

    private PageParam page;
    // 主键Id 
    private Integer id; 
    // 角色名称 
    private String roleName; 
    // 角色描述 
    private String roleDesc; 
    // 所属分组Id 
    private Integer fGroupId; 
    // 创建时间 
    private Integer cT; 
    // 修改时间 
    private Integer uT; 
    
}
