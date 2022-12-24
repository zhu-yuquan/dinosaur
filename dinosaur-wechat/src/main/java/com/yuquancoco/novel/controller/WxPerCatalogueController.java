package com.yuquancoco.novel.controller;

import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.core.page.TableDataInfo;
import com.yuquancoco.novel.domain.PerCatalogue;
import com.yuquancoco.novel.service.IPerCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小说章节Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@RestController
@RequestMapping("/wechat/percatalogue")
public class WxPerCatalogueController extends BaseController {
    @Autowired
    private IPerCatalogueService perCatalogueService;

    /**
     * 查询小说章节列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PerCatalogue perCatalogue) {
        startPage();
        List<PerCatalogue> list = perCatalogueService.selectPerCatalogueList(perCatalogue);
        return getDataTable(list);
    }

    /**
     * 获取小说章节详细信息
     */
    @GetMapping("/view")
    public AjaxResult getInfo(String catalogueId) {
        return AjaxResult.success(perCatalogueService.selectPerCatalogueById(catalogueId));
    }


}
