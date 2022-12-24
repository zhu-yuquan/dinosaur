package com.yuquancoco.novel.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.PerVideo;
import com.yuquancoco.novel.service.IPerVideoService;
import com.yuquancoco.common.utils.poi.ExcelUtil;
import com.yuquancoco.common.core.page.TableDataInfo;

/**
 * 视频Controller
 * 
 * @author zyq
 * @date 2022-02-03
 */
@RestController
@RequestMapping("/novel/pervideo")
public class PerVideoController extends BaseController
{
    @Autowired
    private IPerVideoService perVideoService;

    /**
     * 查询视频列表
     */
    @PreAuthorize("@ss.hasPermi('novel:pervideo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PerVideo perVideo)
    {
        startPage();
        List<PerVideo> list = perVideoService.selectPerVideoList(perVideo);
        return getDataTable(list);
    }

    /**
     * 导出视频列表
     */
    @PreAuthorize("@ss.hasPermi('novel:pervideo:export')")
    @Log(title = "视频", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PerVideo perVideo)
    {
        List<PerVideo> list = perVideoService.selectPerVideoList(perVideo);
        ExcelUtil<PerVideo> util = new ExcelUtil<PerVideo>(PerVideo.class);
        util.exportExcel(response, list, "视频数据");
    }

    /**
     * 获取视频详细信息
     */
    @PreAuthorize("@ss.hasPermi('novel:pervideo:query')")
    @GetMapping(value = "/{videoId}")
    public AjaxResult getInfo(@PathVariable("videoId") Long videoId)
    {
        return AjaxResult.success(perVideoService.selectPerVideoByVideoId(videoId));
    }

    /**
     * 新增视频
     */
    @PreAuthorize("@ss.hasPermi('novel:pervideo:add')")
    @Log(title = "视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PerVideo perVideo)
    {
        return toAjax(perVideoService.insertPerVideo(perVideo));
    }

    /**
     * 修改视频
     */
    @PreAuthorize("@ss.hasPermi('novel:pervideo:edit')")
    @Log(title = "视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PerVideo perVideo)
    {
        return toAjax(perVideoService.updatePerVideo(perVideo));
    }

    /**
     * 删除视频
     */
    @PreAuthorize("@ss.hasPermi('novel:pervideo:remove')")
    @Log(title = "视频", businessType = BusinessType.DELETE)
	@DeleteMapping("/{videoIds}")
    public AjaxResult remove(@PathVariable Long[] videoIds)
    {
        return toAjax(perVideoService.deletePerVideoByVideoIds(videoIds));
    }
}
