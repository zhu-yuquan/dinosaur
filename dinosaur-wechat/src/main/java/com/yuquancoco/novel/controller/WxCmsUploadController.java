package com.yuquancoco.novel.controller;

import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.core.page.TableDataInfo;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.CmsUpload;
import com.yuquancoco.novel.service.ICmsUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@RestController
@RequestMapping("/wechat/cmsupload")
public class WxCmsUploadController extends BaseController {
    @Autowired
    private ICmsUploadService cmsUploadService;

    /**
     * 查询文件列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CmsUpload cmsUpload) {
        startPage();
        List<CmsUpload> list = cmsUploadService.selectCmsUploadList(cmsUpload);
        return getDataTable(list);
    }

    /**
     * 获取文件详细信息
     */
    @GetMapping("/view")
    public AjaxResult getInfo(Long uploadId) {
        return AjaxResult.success(cmsUploadService.selectCmsUploadById(uploadId));
    }

    /**
     * 新增文件
     */
    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public AjaxResult add(@RequestBody CmsUpload cmsUpload) {
        return toAjax(cmsUploadService.insertCmsUpload(cmsUpload));
    }

    /**
     * 删除文件
     */
    @Log(title = "文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uploadIds}")
    public AjaxResult remove(@PathVariable Long[] uploadIds) {
        return toAjax(cmsUploadService.deleteCmsUploadByIds(uploadIds));
    }
}
