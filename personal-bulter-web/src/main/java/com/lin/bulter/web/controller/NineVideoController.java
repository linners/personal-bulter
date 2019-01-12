package com.lin.bulter.web.controller;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.NineVideoService;
import com.lin.bulter.common.dto.NineVideoParam;
import com.lin.bulter.common.dto.PornVideoParam;
import com.lin.bulter.common.dto.common.PageParam;
import com.lin.bulter.repository.mysql.entity.NineVideo;
import com.lin.bulter.repository.mysql.entity.PornVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bulter/nineVideo")
public class NineVideoController {

    @Autowired
    private NineVideoService nineVideoService;

    /**
     * 新增
     * @return
     */
    @PostMapping("/save")
    public Integer saveNineVideo(@RequestBody NineVideo nineVideo) {
        Integer result = nineVideoService.insertNineVideo(nineVideo);
        return result;
    }

    /**
     * 修改
     * @return
     */
    @PostMapping("/update")
    public Integer updateNineVideoById(@RequestBody NineVideo nineVideo) {
        Integer result = nineVideoService.updateNineVideoById(nineVideo);
        return result;
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete/{nineVideoId}")
    public Integer deleteNineVideoById(@PathVariable("nineVideoId") Integer nineVideoId) {
        Integer result = nineVideoService.deleteNineVideoById(nineVideoId);
        return result;
    }

    /**
     * 按主键查询
     * @return
     */
    @GetMapping("/getNineVideoById/{nineVideoId}")
    public NineVideo getNineVideoById(@PathVariable("nineVideoId") Integer nineVideoId) {
        NineVideo result = nineVideoService.selectNineVideoById(nineVideoId);
        return result;
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/getAllNineVideos")
    public List<NineVideo> getAllNineVideos() {
        List<NineVideo> result = nineVideoService.selectAllNineVideos();
        return result;
    }

    /**
     * 分页查询
     * @param param
     * @return
     */
    @PostMapping("/getNineVideoByPage")
    public PageInfo<NineVideo> getNineVideoByPage(NineVideoParam param) {
        PageInfo<NineVideo> result = nineVideoService.selectNineVideoByPage(param);
        return result;
    }

    @GetMapping("/list/{page}")
    public PageInfo<NineVideo> pageList(@PathVariable("page") Integer pageNo) {
        NineVideoParam param = new NineVideoParam();
        PageParam page = new PageParam();
        page.setPageNum(pageNo);
        page.setPageSize(10);
        param.setPage(page);
        PageInfo<NineVideo> result = nineVideoService.selectNineVideoByPage(param);
        return result;
    }
}
