package com.lin.bulter.business.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.common.dto.NineVideoParam;
import com.lin.bulter.repository.mysql.entity.NineVideo;

import java.util.List;

public interface NineVideoService {

    /**
     * 新增
     *
     * @return
     */
    Integer insertNineVideo(NineVideo nineVideo);

    /**
     * 修改
     *
     * @return
     */
    Integer updateNineVideoById(NineVideo nineVideo);

    /**
     * 删除
     */
    Integer deleteNineVideoById(Integer nineVideoId);

    /**
     * 按主键查询
     */
    NineVideo selectNineVideoById(Integer nineVideoId);

    /**
     * 查询所有
     */
    List<NineVideo> selectAllNineVideos();

    /**
     * 分页查询
     */
    PageInfo<NineVideo> selectNineVideoByPage(NineVideoParam nineVideoParam);
}
