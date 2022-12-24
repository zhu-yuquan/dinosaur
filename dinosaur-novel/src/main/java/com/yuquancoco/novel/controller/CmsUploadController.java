package com.yuquancoco.novel.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.CmsUpload;
import com.yuquancoco.novel.service.ICmsUploadService;
import com.yuquancoco.common.utils.poi.ExcelUtil;
import com.yuquancoco.common.core.page.TableDataInfo;

/**
 * 文件Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@RestController
@RequestMapping("/novel/cmsupload")
public class CmsUploadController extends BaseController {
    @Autowired
    private ICmsUploadService cmsUploadService;

    /**
     * 查询文件列表
     */
    @PreAuthorize("@ss.hasPermi('novel:cmsupload:list')")
    @GetMapping("/list")
        public TableDataInfo list(CmsUpload cmsUpload) {
        startPage();
        List<CmsUpload> list = cmsUploadService.selectCmsUploadList(cmsUpload);
        return getDataTable(list);
    }
    
    /**
     * 导出文件列表
     */
    @PreAuthorize("@ss.hasPermi('novel:cmsupload:export')")
    @Log(title = "文件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CmsUpload cmsUpload) {
        List<CmsUpload> list = cmsUploadService.selectCmsUploadList(cmsUpload);
        ExcelUtil<CmsUpload> util = new ExcelUtil<CmsUpload>(CmsUpload. class);
        return util.exportExcel(list, "文件数据");
    }

    /**
     * 获取文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('novel:cmsupload:query')")
    @GetMapping(value = "/{uploadId}")
    public AjaxResult getInfo(@PathVariable("uploadId") Long uploadId) {
        return AjaxResult.success(cmsUploadService.selectCmsUploadById(uploadId));
    }

    /**
     * 新增文件
     */
    @PreAuthorize("@ss.hasPermi('novel:cmsupload:add')")
    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsUpload cmsUpload) {
        return toAjax(cmsUploadService.insertCmsUpload(cmsUpload));
    }

    /**
     * 修改文件
     */
    @PreAuthorize("@ss.hasPermi('novel:cmsupload:edit')")
    @Log(title = "文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsUpload cmsUpload) {
        return toAjax(cmsUploadService.updateCmsUpload(cmsUpload));
    }

    /**
     * 删除文件
     */
    @PreAuthorize("@ss.hasPermi('novel:cmsupload:remove')")
    @Log(title = "文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uploadIds}")
    public AjaxResult remove(@PathVariable Long[] uploadIds) {
        return toAjax(cmsUploadService.deleteCmsUploadByIds(uploadIds));
    }
}
