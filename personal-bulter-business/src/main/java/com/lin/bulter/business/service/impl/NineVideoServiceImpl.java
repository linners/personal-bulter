package com.lin.bulter.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.NineVideoService;
import com.lin.bulter.common.dto.NineVideoParam;
import com.lin.bulter.repository.mysql.dao.NineVideoMapper;
import com.lin.bulter.repository.mysql.entity.NineVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NineVideoServiceImpl implements NineVideoService {

    @Autowired
    private NineVideoMapper nineVideoMapper;

    @Override
    @Transactional
    public Integer insertNineVideo(NineVideo nineVideo) {
        return nineVideoMapper.insert(nineVideo);
    }

    @Override
    @Transactional
    public Integer updateNineVideoById(NineVideo nineVideo) {
        return nineVideoMapper.updateByPrimaryKeySelective(nineVideo);
    }

    @Override
    @Transactional
    public Integer deleteNineVideoById(Integer nineVideoId) {
        return nineVideoMapper.deleteByPrimaryKey(nineVideoId);
    }

    @Override
    public NineVideo selectNineVideoById(Integer nineVideoId) {
        return nineVideoMapper.selectByPrimaryKey(nineVideoId);
    }

    @Override
    public List<NineVideo> selectAllNineVideos() {
        return nineVideoMapper.selectAll();
    }

    @Override
    public PageInfo<NineVideo> selectNineVideoByPage(NineVideoParam nineVideoParam) {
        PageInfo<NineVideo> nineVideos = PageHelper.startPage(nineVideoParam.getPage().getPageNum(), nineVideoParam.getPage().getPageSize())
                .doSelectPageInfo(() -> nineVideoMapper.selectByCondition(nineVideoParam));
        return nineVideos;
    }
}
