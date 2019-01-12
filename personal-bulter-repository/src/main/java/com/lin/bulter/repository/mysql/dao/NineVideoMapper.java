package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.common.dto.NineVideoParam;
import com.lin.bulter.repository.mysql.entity.NineVideo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NineVideoMapper {
    
    /**
     *  按主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入一条记录
     */
    int insert(NineVideo record);

    /**
     *  按主键查询
     */
    NineVideo selectByPrimaryKey(Integer id);

    /**
     *  查询所有记录
     */
    List<NineVideo> selectAll();

    /**
     *  按主键更新
     */
    int updateByPrimaryKeySelective(NineVideo record);

    /**
     *  按条件分页查询
     */
    List<NineVideo> selectByCondition(@Param("param") NineVideoParam nineVideoParam);
}
