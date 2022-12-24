package com.yuquancoco.novel.controller;

import java.io.IOException;
import java.util.List;

import com.yuquancoco.novel.domain.PerCatalogue;
import com.yuquancoco.novel.domain.SSLHelper;
import com.yuquancoco.novel.service.IPerCatalogueService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yuquancoco.common.annotation.Log;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.enums.BusinessType;
import com.yuquancoco.novel.domain.PerNovel;
import com.yuquancoco.novel.service.IPerNovelService;
import com.yuquancoco.common.utils.poi.ExcelUtil;
import com.yuquancoco.common.core.page.TableDataInfo;

/**
 * 小说Controller
 *
 * @author zyq
 * @date 2021-08-16
 */
@Slf4j
@RestController
@RequestMapping("/novel/pernovel")
public class PerNovelController extends BaseController {
    @Autowired
    private IPerNovelService perNovelService;
    @Autowired
    private IPerCatalogueService perCatalogueService;

    /**
     * 查询小说列表
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:list')")
    @GetMapping("/list")
    public TableDataInfo list(PerNovel perNovel) {
        startPage();
        List<PerNovel> list = perNovelService.selectPerNovelList(perNovel);
        return getDataTable(list);
    }

    /**
     * 查询小说列表
     */
    @GetMapping("/all_list")
    public AjaxResult allList(PerNovel perNovel) {
        List<PerNovel> list = perNovelService.selectPerNovelList(perNovel);
        return AjaxResult.success(list);
    }

    /**
     * 更新小说章节
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:edit')")
    @Log(title = "小说章节更新", businessType = BusinessType.UPDATE)
    @GetMapping("/update_catalogue")
    public void getPercateLogueList(@RequestParam("novelId") String novelId) {
        String url = "https://www.quge7.com";
        PerNovel perNovel = perNovelService.selectPerNovelById(novelId);
        Document document = null;
        try {
            document = Jsoup.connect(url + perNovel.getBookCode()).get();
//            log.info("outerHtml=" + document.outerHtml());
            // 描述
            Elements intros = document.getElementsByAttributeValue("class", "intro");
            Element intro = intros.first();
            String bookIntro = intro.select("dd").text();
            perNovel.setBookIntro(bookIntro);
//            log.info("bookIntro=" + bookIntro);

            // 所有章节
            Elements cataloguelist = document.getElementsByAttributeValue("class", "listmain");
            Element list = cataloguelist.first();
            // 获取目录列表和每个目录章节的链接
            Elements dd = list.getElementsByTag("dd");
            for (int k = 0; k < dd.size(); k++) {
                if (1 == 1) {
                    Element element = dd.get(k);
                    PerCatalogue perCatalogue = new PerCatalogue();
                    perCatalogue.setNovelId(perNovel.getNovelId());
                    Element urlthml = element.child(0);
                    String pageUrl = urlthml.attributes().get("href");
                    if ("javascript:dd_show()".equals(pageUrl)) {
                        continue;
                    }
//                    log.info("element.text()===链接地址====" + pageUrl);
                    perCatalogue.setCatalogueUrl(pageUrl);
                    perCatalogue.setCatalogueName(element.text()); // 章节名称
//                    log.info("element.text()===章节名称====" + element.text());

                    //获取章节内容
                    Element content = null;
                    try {
                        SSLHelper.init();
                        content = Jsoup.connect(url + pageUrl).get();
                    } catch (IOException e) {
                        try {
                            content = Jsoup.connect(url + pageUrl).get();
                        } catch (IOException e1) {
                            perCatalogue.setCatalogueText("暂无内容");
                            perCatalogueService.insertPerCatalogue(perCatalogue);
                            continue;
                        }
                    }
                    Element contentElement = content.getElementById("chaptercontent");
                    List<PerCatalogue> perCatalogueList = perCatalogueService.selectPerCatalogueList(perCatalogue);
                    if (perCatalogueList.size() == 0) {
                        perCatalogue.setCatalogueText(contentElement.text());
                        perCatalogueService.insertPerCatalogue(perCatalogue);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出小说列表
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:export')")
    @Log(title = "小说", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PerNovel perNovel) {
        List<PerNovel> list = perNovelService.selectPerNovelList(perNovel);
        ExcelUtil<PerNovel> util = new ExcelUtil<PerNovel>(PerNovel.class);
        return util.exportExcel(list, "小说数据");
    }

    /**
     * 获取小说详细信息
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:query')")
    @GetMapping(value = "/{novelId}")
    public AjaxResult getInfo(@PathVariable("novelId") String novelId) {
        return AjaxResult.success(perNovelService.selectPerNovelById(novelId));
    }

    /**
     * 新增小说
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:add')")
    @Log(title = "小说", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PerNovel perNovel) {
        return toAjax(perNovelService.insertPerNovel(perNovel));
    }

    /**
     * 修改小说
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:edit')")
    @Log(title = "小说", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PerNovel perNovel) {
        return toAjax(perNovelService.updatePerNovel(perNovel));
    }

    /**
     * 删除小说
     */
    @PreAuthorize("@ss.hasPermi('novel:pernovel:remove')")
    @Log(title = "小说", businessType = BusinessType.DELETE)
    @DeleteMapping("/{novelIds}")
    public AjaxResult remove(@PathVariable String[] novelIds) {
        return toAjax(perNovelService.deletePerNovelByIds(novelIds));
    }
}
