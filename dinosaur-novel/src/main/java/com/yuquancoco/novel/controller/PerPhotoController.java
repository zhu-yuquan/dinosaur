package com.yuquancoco.novel.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.PerPhoto;
import com.yuquancoco.novel.service.IPerPhotoService;
import com.yuquancoco.common.utils.poi.ExcelUtil;
import com.yuquancoco.common.core.page.TableDataInfo;

/**
 * 相册Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@RestController
@RequestMapping("/novel/perphoto")
public class PerPhotoController extends BaseController {
    @Autowired
    private IPerPhotoService perPhotoService;

    /**
     * 查询相册列表
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:list')")
    @GetMapping("/list")
        public TableDataInfo list(PerPhoto perPhoto) {
        startPage();
        List<PerPhoto> list = perPhotoService.selectPerPhotoList(perPhoto);
        return getDataTable(list);
    }

    /**
     * 查询相册列表
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:list')")
    @GetMapping("/alllist")
    public AjaxResult getAllList(PerPhoto perPhoto) {
        List<PerPhoto> list = perPhotoService.selectPerPhotoList(perPhoto);
        return AjaxResult.success(list);
    }
    
    /**
     * 导出相册列表
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:export')")
    @Log(title = "相册", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PerPhoto perPhoto) {
        List<PerPhoto> list = perPhotoService.selectPerPhotoList(perPhoto);
        ExcelUtil<PerPhoto> util = new ExcelUtil<PerPhoto>(PerPhoto. class);
        return util.exportExcel(list, "相册数据");
    }

    /**
     * 获取相册详细信息
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:query')")
    @GetMapping(value = "/{photoId}")
    public AjaxResult getInfo(@PathVariable("photoId") Long photoId) {
        return AjaxResult.success(perPhotoService.selectPerPhotoById(photoId));
    }

    /**
     * 新增相册
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:add')")
    @Log(title = "相册", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PerPhoto perPhoto) {
        return toAjax(perPhotoService.insertPerPhoto(perPhoto));
    }

    /**
     * 修改相册
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:edit')")
    @Log(title = "相册", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PerPhoto perPhoto) {
        return toAjax(perPhotoService.updatePerPhoto(perPhoto));
    }

    /**
     * 删除相册
     */
    @PreAuthorize("@ss.hasPermi('novel:perphoto:remove')")
    @Log(title = "相册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{photoIds}")
    public AjaxResult remove(@PathVariable Long[] photoIds) {
        return toAjax(perPhotoService.deletePerPhotoByIds(photoIds));
    }
}
