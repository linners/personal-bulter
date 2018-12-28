package com.lin.bulter.business.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.common.dto.PornVideoParam;
import com.lin.bulter.repository.mysql.entity.PornVideo;

import java.util.List;

public interface PornVideoService {

    /**
     * 查询所有video
     */
    List<PornVideo> selectAllPornVideos();

    /**
     * 查询收藏的video
     */
    PageInfo<PornVideo> selectFavoritePornVideos(PornVideoParam param);

    /**
     * 分页查询
     */
    PageInfo<PornVideo> selectPornVideoByPage(PornVideoParam param);

    /**
     * 收藏
     * @param id
     * @return
     */
    Integer dofavorite(Integer id);
}
