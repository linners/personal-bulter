package com.lin.bulter.repository.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
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
