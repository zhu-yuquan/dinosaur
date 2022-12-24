package com.yuquancoco.novel.controller;

import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.core.page.TableDataInfo;
import com.yuquancoco.novel.domain.PerNovel;
import com.yuquancoco.novel.service.WxIPerNovelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小说Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@Slf4j
@RestController
@RequestMapping("/wechat/pernovel")
public class WxPerNovelController extends BaseController {
    @Autowired
    private WxIPerNovelService perNovelService;

    /**
     * 查询小说列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PerNovel perNovel) {
        startPage();
        List<PerNovel> list = perNovelService.selectPerNovelList(perNovel);
        return getDataTable(list);
    }


    /**
     * 获取小说详细信息
     */
    @GetMapping("/view")
    public AjaxResult getInfo(String novelId) {
        return AjaxResult.success(perNovelService.selectPerNovelById(novelId));
    }


}
