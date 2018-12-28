package com.lin.bulter.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.PornVideoService;
import com.lin.bulter.common.dto.PornVideoParam;
import com.lin.bulter.repository.mysql.dao.PornVideoMapper;
import com.lin.bulter.repository.mysql.entity.PornVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PornVideoServiceImpl implements PornVideoService {

    @Autowired
    private PornVideoMapper pornVideoMapper;

    @Override
    public List<PornVideo> selectAllPornVideos() {
        return pornVideoMapper.selectAll();
    }

    @Override
    public PageInfo<PornVideo>  selectFavoritePornVideos(PornVideoParam param) {
        PageInfo<PornVideo> pornVideos = PageHelper.startPage(param.getPage().getPageNum(), param.getPage().getPageSize())
                .doSelectPageInfo(() -> pornVideoMapper.selectFavoritePornVideos());
        return pornVideos;
    }

    @Override
    public PageInfo<PornVideo> selectPornVideoByPage(PornVideoParam param) {
        PageInfo<PornVideo> pornVideos = PageHelper.startPage(param.getPage().getPageNum(), param.getPage().getPageSize())
                .doSelectPageInfo(() -> pornVideoMapper.selectAll());
        return pornVideos;
    }

    @Override
    @Transactional
    public Integer dofavorite(Integer id) {
        return pornVideoMapper.dofavorite(id);
    }
}
