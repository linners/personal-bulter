package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.repository.mysql.entity.TableInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratorMapper {

    /**
     * 获得数据库表所有信息
     * @param tableName
     * @return
     */
    List<TableInfo> getTableInfo(String tableName);
}
