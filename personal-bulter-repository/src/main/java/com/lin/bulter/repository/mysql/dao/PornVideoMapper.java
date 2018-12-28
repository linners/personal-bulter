package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.repository.mysql.entity.PornVideo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PornVideoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_video_2
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_video_2
     *
     * @mbg.generated
     */
    int insert(PornVideo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_video_2
     *
     * @mbg.generated
     */
    int insertSelective(PornVideo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_video_2
     *
     * @mbg.generated
     */
    PornVideo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_video_2
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(PornVideo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_video_2
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(PornVideo record);

    /**
     * 查询所有
     *
     * @return
     */
    List<PornVideo> selectAll();

    /**
     * 查询最爱video
     * @return
     */
    List<PornVideo> selectFavoritePornVideos();

    /**
     * 收藏
     * @param id
     * @return
     */
    Integer dofavorite(Integer id);
}
