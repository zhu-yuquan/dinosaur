package com.yuquancoco.novel.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.PerCatalogue;
import com.yuquancoco.novel.service.IPerCatalogueService;
import com.yuquancoco.common.utils.poi.ExcelUtil;
import com.yuquancoco.common.core.page.TableDataInfo;

/**
 * 小说章节Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@RestController
@RequestMapping("/novel/percatalogue")
public class PerCatalogueController extends BaseController {
    @Autowired
    private IPerCatalogueService perCatalogueService;

    /**
     * 查询小说章节列表
     */
    @PreAuthorize("@ss.hasPermi('novel:percatalogue:list')")
    @GetMapping("/list")
        public TableDataInfo list(PerCatalogue perCatalogue) {
        startPage();
        List<PerCatalogue> list = perCatalogueService.selectPerCatalogueList(perCatalogue);
        return getDataTable(list);
    }
    
    /**
     * 导出小说章节列表
     */
    @PreAuthorize("@ss.hasPermi('novel:percatalogue:export')")
    @Log(title = "小说章节", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PerCatalogue perCatalogue) {
        List<PerCatalogue> list = perCatalogueService.selectPerCatalogueList(perCatalogue);
        ExcelUtil<PerCatalogue> util = new ExcelUtil<PerCatalogue>(PerCatalogue. class);
        return util.exportExcel(list, "小说章节数据");
    }

    /**
     * 获取小说章节详细信息
     */
    @PreAuthorize("@ss.hasPermi('novel:percatalogue:query')")
    @GetMapping(value = "/{catalogueId}")
    public AjaxResult getInfo(@PathVariable("catalogueId") String catalogueId) {
        return AjaxResult.success(perCatalogueService.selectPerCatalogueById(catalogueId));
    }

    /**
     * 新增小说章节
     */
    @PreAuthorize("@ss.hasPermi('novel:percatalogue:add')")
    @Log(title = "小说章节", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PerCatalogue perCatalogue) {
        return toAjax(perCatalogueService.insertPerCatalogue(perCatalogue));
    }

    /**
     * 修改小说章节
     */
    @PreAuthorize("@ss.hasPermi('novel:percatalogue:edit')")
    @Log(title = "小说章节", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PerCatalogue perCatalogue) {
        return toAjax(perCatalogueService.updatePerCatalogue(perCatalogue));
    }

    /**
     * 删除小说章节
     */
    @PreAuthorize("@ss.hasPermi('novel:percatalogue:remove')")
    @Log(title = "小说章节", businessType = BusinessType.DELETE)
    @DeleteMapping("/{catalogueIds}")
    public AjaxResult remove(@PathVariable String[] catalogueIds) {
        return toAjax(perCatalogueService.deletePerCatalogueByIds(catalogueIds));
    }
}
