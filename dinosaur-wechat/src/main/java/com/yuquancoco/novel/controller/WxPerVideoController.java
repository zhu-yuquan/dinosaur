package com.yuquancoco.novel.controller;

import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.core.page.TableDataInfo;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.PerVideo;
import com.yuquancoco.novel.service.IPerVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 视频Controller
 *
 * @author zyq
 * @date 2022-02-03
 */
@RestController
@RequestMapping("/wechat/pervideo")
public class WxPerVideoController extends BaseController {
    @Autowired
    private IPerVideoService perVideoService;

    /**
     * 查询视频列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PerVideo perVideo) {
        startPage();
        List<PerVideo> list = perVideoService.selectPerVideoList(perVideo);
        return getDataTable(list);
    }


    /**
     * 获取视频详细信息
     */
    @GetMapping("/view")
    public AjaxResult getInfo(Long videoId) {
        return AjaxResult.success(perVideoService.selectPerVideoByVideoId(videoId));
    }

    /**
     * 新增视频
     */
    @Log(title = "视频", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public AjaxResult add(@RequestBody PerVideo perVideo) {
        return toAjax(perVideoService.insertPerVideo(perVideo));
    }

    /**
     * 删除视频
     */
    @Log(title = "视频", businessType = BusinessType.DELETE)
    @DeleteMapping("/{videoIds}")
    public AjaxResult remove(@PathVariable Long[] videoIds) {
        return toAjax(perVideoService.deletePerVideoByVideoIds(videoIds));
    }
}
