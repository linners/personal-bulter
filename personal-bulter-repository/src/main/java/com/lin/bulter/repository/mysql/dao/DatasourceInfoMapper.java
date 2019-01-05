package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.repository.mysql.entity.DatasourceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_datasource_info
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_datasource_info
     *
     * @mbggenerated
     */
    int insert(DatasourceInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_datasource_info
     *
     * @mbggenerated
     */
    int insertSelective(DatasourceInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_datasource_info
     *
     * @mbggenerated
     */
    DatasourceInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_datasource_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DatasourceInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_datasource_info
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DatasourceInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbggenerated
     */
    List<DatasourceInfo> selectAll();
}