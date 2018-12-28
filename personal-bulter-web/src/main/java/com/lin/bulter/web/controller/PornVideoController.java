package com.lin.bulter.web.controller;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.PornVideoService;
import com.lin.bulter.common.dto.PornVideoParam;
import com.lin.bulter.common.dto.common.PageParam;
import com.lin.bulter.repository.mysql.entity.PornVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bulter/pornvideo")
public class PornVideoController {

    @Autowired
    private PornVideoService pornVideoService;

    @GetMapping("/list/{page}")
    public PageInfo<PornVideo> pageList(@PathVariable("page") Integer pageNo) {
        PornVideoParam param = new PornVideoParam();
        PageParam page = new PageParam();
        page.setPageNum(pageNo);
        page.setPageSize(10);
        param.setPage(page);
        PageInfo<PornVideo> result = pornVideoService.selectPornVideoByPage(param);
        return result;
    }

    @GetMapping("/favorite/{page}")
    public PageInfo<PornVideo> pageFavorite(@PathVariable("page") Integer pageNo) {
        PornVideoParam param = new PornVideoParam();
        PageParam page = new PageParam();
        page.setPageNum(pageNo);
        page.setPageSize(10);
        param.setPage(page);
        PageInfo<PornVideo> result = pornVideoService.selectFavoritePornVideos(param);
        return result;
    }

    @GetMapping("/dofavorite/{id}")
    public Integer dofavorite(@PathVariable("id") Integer id) {
        Integer result = pornVideoService.dofavorite(id);
        return result;
    }
}
