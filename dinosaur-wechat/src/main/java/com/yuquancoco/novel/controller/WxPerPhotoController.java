package com.yuquancoco.novel.controller;

import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.core.page.TableDataInfo;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.PerPhoto;
import com.yuquancoco.novel.service.IPerPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 相册Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@RestController
@RequestMapping("/wechat/perphoto")
public class WxPerPhotoController extends BaseController {
    @Autowired
    private IPerPhotoService perPhotoService;

    /**
     * 查询相册列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PerPhoto perPhoto) {
        startPage();
        List<PerPhoto> list = perPhotoService.selectPerPhotoList(perPhoto);
        return getDataTable(list);
    }

    /**
     * 获取相册详细信息
     */
    @GetMapping("/view")
    public AjaxResult getInfo(Long photoId) {
        return AjaxResult.success(perPhotoService.selectPerPhotoById(photoId));
    }

    /**
     * 新增相册
     */
    @Log(title = "相册", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public AjaxResult add(@RequestBody PerPhoto perPhoto) {
        return toAjax(perPhotoService.insertPerPhoto(perPhoto));
    }

    /**
     * 修改相册
     */
    @Log(title = "相册", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody PerPhoto perPhoto) {
        return toAjax(perPhotoService.updatePerPhoto(perPhoto));
    }

    /**
     * 删除相册
     */
    @Log(title = "相册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{photoIds}")
    public AjaxResult remove(@PathVariable Long[] photoIds) {
        return toAjax(perPhotoService.deletePerPhotoByIds(photoIds));
    }
}
