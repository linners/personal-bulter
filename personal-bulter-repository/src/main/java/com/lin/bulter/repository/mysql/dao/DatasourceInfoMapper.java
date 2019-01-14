package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.common.dto.DatasourceInfoParam;
import com.lin.bulter.repository.mysql.entity.DatasourceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceInfoMapper {
    
    /**
     *  按主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入一条记录
     */
    int insert(DatasourceInfo record);

    /**
     *  按主键查询
     */
    DatasourceInfo selectByPrimaryKey(Integer id);

    /**
     *  查询所有记录
     */
    List<DatasourceInfo> selectAll();

    /**
     *  按主键更新
     */
    int updateByPrimaryKeySelective(DatasourceInfo record);

    /**
     *  按条件分页查询
     */
    List<DatasourceInfo> selectByCondition(@Param("param") DatasourceInfoParam datasourceInfoParam);

    Integer insertSelective(DatasourceInfo datasourceInfo);

    void updateByPrimaryKey(DatasourceInfo datasourceInfo);
}
