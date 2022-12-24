package com.yuquancoco.novel.controller;

import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.utils.DateUtils;
import com.yuquancoco.common.utils.file.FileUploadUtils;
import com.yuquancoco.novel.domain.PerCatalogue;
import com.yuquancoco.novel.domain.PerNovel;
import com.yuquancoco.novel.domain.SSLHelper;
import com.yuquancoco.novel.service.DownloadBookService;
import com.yuquancoco.novel.service.IPerCatalogueService;
import com.yuquancoco.novel.service.IPerNovelService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author zyq
 * @Title: ReptileNovelController
 * @Description: 爬取小说
 * @date 2021/8/10 12:31
 */
@RestController
@Slf4j
public class ReptileNovelController {

    @Autowired
    private DownloadBookService downloadBookService;
    @Autowired
    private IPerNovelService perNovelService;
    @Autowired
    private IPerCatalogueService perCatalogueService;

    @GetMapping("/noveldownload")
    public AjaxResult downloadBookList() {
        //
        final Executor executor = Executors.newCachedThreadPool();//启用多线程
        for (int i = 1; i < 0; i++) {
            final int j = i;  // 关键是这一句代码，将 i 转化为  j，这样j 还是final类型的参与线程
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("" + j);

                    } catch (Exception e) {

                    }
                }
            });
        }

        this.getNovelList();

//        this.getPercateLogueList();

        return AjaxResult.success();
    }

    public void getNovelList() {
        String url = "https://www.quge7.com";
        for (int i = 67298; i < 100000; i++) {
            //cateType 0(完本) 1(玄幻) 2(无限) 3(都市) 4(历史) 5(网游) 6(科幻) 7(女生)
            if (i >= 1) {
                try {
                    System.out.println(url + "/book/" + i + "/");
                    SSLHelper.init();
                    Document document = Jsoup.connect(url + "/book/" + i + "/").get();
//                    log.info("outerHtml=" + document.outerHtml());
                    Elements cateElements = document.getElementsByAttributeValue("class", "path wap_none");
                    String[] cateStr = cateElements.get(0).text().split(">");
//                    log.info("cateStr=" + cateStr[1].trim());
                    String cateName = cateStr[1].trim();
                    Elements bookElements = document.getElementsByAttributeValue("class", "info");
                    Elements bookNameE = bookElements.get(0).select("h1");
//                    log.info("bookName:" + bookNameE.text());
                    String bookName = bookNameE.text();

                    Elements authorElements = document.getElementsByAttributeValue("class", "small");
                    Element authorE = authorElements.first().child(0);
                    String[] authors = authorE.text().split("：");
                    String bookAuthor = "";
                    if (authors.length > 1) {
                        bookAuthor = authors[1];
                    }
                    Element statusE = authorElements.first().child(1);
                    String[] statuss = statusE.text().split("：");
                    String bookStatus = statuss[1];
                    String bookCode = "/book/" + i + "/";

                    Elements bookImgE = bookElements.select("img");
//                    log.info("bookImage:" + bookImgE.get(0).attributes().get("src"));
                    String bookImage = bookImgE.get(0).attributes().get("src");
                    PerNovel perNovel = new PerNovel();
                    perNovel.setBookName(bookName);
                    perNovel.setBookAuthor(bookAuthor);
                    perNovel.setBookCode(bookCode);
                    perNovel.setBookImage(bookImage);
                    perNovel.setBookStatus(bookStatus);
                    perNovel.setCateName(cateName);
                    perNovel.setDelFlag("N");
                    perNovel.setCreateTime(new Date());
                    perNovel.setCreateBy("程序入库");
                    // 描述
                    Elements intros = document.getElementsByAttributeValue("class", "intro");
                    Element intro = intros.first();
                    String bookIntro = intro.select("dd").text();
                    List<PerNovel> perNovelList = perNovelService.selectPerNovelList(perNovel);
                    if (perNovelList.size() == 0) {
                        perNovel.setBookIntro(bookIntro);
//                        log.info("bookIntro=" + bookIntro);
                        perNovelService.insertPerNovel(perNovel);
                    } else {
                        perNovel = perNovelList.get(0);
                    }

                    // 所有章节
                    if (2 < 1) {
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
                                perCatalogue.setCatalogueUrl(pageUrl);
//                                log.info("element.text()===链接地址====" + pageUrl);
                                perCatalogue.setCatalogueName(element.text()); // 章节名称
//                                log.info("element.text()===章节名称====" + element.text());

                                //获取章节内容
                                Element content = null;
                                try {
                                    content = Jsoup.connect(url + pageUrl).get();
                                } catch (IOException e) {
                                    try {
                                        content = Jsoup.connect(url + pageUrl).get();
                                    } catch (IOException e1) {
                                        return;
                                    }
                                }
                                Element contentElement = content.getElementById("chaptercontent");
                                List<PerCatalogue> perCatalogueList = perCatalogueService.selectPerCatalogueList(perCatalogue);
                                if (perCatalogueList.size() == 0) {
//                                    log.info("contentElement------------------------=" + contentElement.text());
                                    perCatalogue.setCatalogueText(contentElement.text());
                                    perCatalogueService.insertPerCatalogue(perCatalogue);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("load fail :" + url + "/book/" + i + "/");
                    System.out.println("load fail-------------:" + e.getMessage());
                }
            }
        }
    }

    public void getPercateLogueList() {
        String url = "https://www.quge7.com";
        List<PerNovel> perNovelList = perNovelService.selectPerNovelList(new PerNovel());
        for (PerNovel perNovel : perNovelList) {
            if (2 > 1) {
                Document document = null;
                try {
                    document = Jsoup.connect(url + perNovel.getBookCode()).get();
//                    log.info("outerHtml=" + document.outerHtml());
                    // 描述
                    Elements intros = document.getElementsByAttributeValue("class", "intro");
                    Element intro = intros.first();
                    String bookIntro = intro.select("dd").text();
                    perNovel.setBookIntro(bookIntro);
//                    log.info("bookIntro=" + bookIntro);
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
                            log.info("element.text()===链接地址====" + pageUrl);
                            perCatalogue.setCatalogueUrl(pageUrl);
                            perCatalogue.setCatalogueName(element.text()); // 章节名称
                            log.info("element.text()===章节名称====" + element.text());
                            //获取章节内容
                            Element content = null;
                            try {
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
//                                log.info("contentElement------------------------=" + contentElement.text());
                                perCatalogue.setCatalogueText(contentElement.text());
                                perCatalogueService.insertPerCatalogue(perCatalogue);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @GetMapping("/reptile-download")
    public void userList() {
//        reptileList();
        this.getNovelList();
//        this.getPercateLogueList();

    }

    public void saveErrorMessage(String title, String message) {
        String fileName = null;
        String filePath = FileUploadUtils.getDefaultBaseDir(); // 保存文件路径
        String exName = "txt"; // 文本
        fileName = title + "_error_log." + exName; // 文件名称
        try {
            File file = new File(filePath + "/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            // 开始记载报错日志
            FileWriter pw = new FileWriter(file, true);
            pw.write(message);
            pw.close();
        } catch (IOException e) {
            System.out.println("save error message fail：" + e.getMessage());
        }
    }


    /**
     * 七虎影视
     */
    public void reptileList() {
//        http://xingdatiyu.com/

        final String url = "https://www.x7h88.com";
        final Executor executor = Executors.newCachedThreadPool();//启用多线程
        try {
            SSLHelper.init();
            Document document = Jsoup.connect(url).get();
            Elements cateTypeList = document.getElementsByAttributeValue("class", "menu clearfix");
            Elements imglist = cateTypeList.select("a");
            for (int i = 0; i < imglist.size(); i++) {
                Element element = imglist.get(i);
                String href = element.attributes().get("href");
                String title = element.text();
                //   /video/zipai/   /video/fuqi/  /video/kaifang/  /video/jingpin/
                //   /video/twmn/  /video/krzb/    /video/dongman/   /video/sanji/

                //   /av/nxx/  /av/bdyjy/  /av/stym/  /av/qbyc/
                //   /av/cjk/  /av/ssyy/  /av/thy/   /av/jzmb/

                //  /movie/wuma/  /movie/sm/  /movie/gaoqing/  /movie/shunv/
                //  /movie/meiyan/  /movie/siwa/  /movie/youma/  /movie/oumei/

                //  /pic/toupai/  /pic/meitui/  /pic/oumei/  /pic/katong/
                //  /pic/dushi/  /pic/luanlun/  /pic/xiaoyuan/  /pic/renqi/

                if (href.indexOf("/movie") != -1) {
                    if (!"javascript:void(0);".equals(href)) {
                        SSLHelper.init();
                        Document pageDocument = Jsoup.connect(url + href).get();
                        Elements elements = pageDocument.getElementsByTag("a");
                        Element element1 = elements.last();
                        String lastHref = element1.attributes().get("href");
                        System.out.println("lastHref=" + lastHref.substring(lastHref.indexOf("_") + 1, lastHref.indexOf(".")));
                        int num = Integer.parseInt(lastHref.substring(lastHref.indexOf("_") + 1, lastHref.indexOf(".")));
//                        System.out.println("num=" + num);
//                        num = 3;

                        // chinese
                        if (href.indexOf("video") != -1) {
                            if (href.indexOf("zipai") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;

                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
//                                    downloadBookService.getvideo(url, href, j);
                                }
                            }
                            if (href.indexOf("fuqi") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("kaifang") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("jingpin") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("twmn") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("krzb") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("dongman") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("sanji") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        // 女优
                        if (href.indexOf("av") != -1) {
                            log.info("女优链接地址=" + href);
                            if (href.indexOf("nxx") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("bdyjy") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("stym") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("qbyc") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("cjk") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("ssyy") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("thy") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("jzmb") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }

                        }
                        // 电影
                        if (href.indexOf("movie") != -1) {
                            log.info("电影链接地址=" + href);
                            if (href.indexOf("wuma") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("sm") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("gaoqing") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("shunv") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("meiyan") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("siwa") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("youma") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            if (href.indexOf("oumei") != -1) {
                                for (int j = 1; j < num; j++) {
                                    final int ss = j;
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // to do
                                                downloadBookService.getvideo(url, href, ss);
                                            } catch (Exception e) {
                                                System.out.println("下载报错:" + href);
                                                System.out.println("loadFailMessage:" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        // 图文
                        if (href.indexOf("pic") != -1) {
                            if (title.indexOf("小说") == -1) {
                                log.info("图文链接地址=" + href);
                                // toupai  meitui  oumei  katong
                                if (href.indexOf("toupai") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downloadPic(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downloadPic(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                if (href.indexOf("meitui") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downloadPic(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downloadPic(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                if (href.indexOf("oumei") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downloadPic(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downloadPic(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                if (href.indexOf("katong") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downloadPic(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downloadPic(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            } else {
                                log.info("小说链接地址=" + href);
                                if (href.indexOf("dushi") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downLoadNovel(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downLoadNovel(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                if (href.indexOf("luanlun") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downLoadNovel(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downLoadNovel(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                if (href.indexOf("xiaoyuan") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downLoadNovel(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downLoadNovel(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                                if (href.indexOf("renqi") != -1) {
                                    for (int j = 1; j < num; j++) {
//                                        downloadBookService.downLoadNovel(url, href, j);
                                        final int ss = j;
                                        executor.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // to do
                                                    downloadBookService.downLoadNovel(url, href, ss);
                                                } catch (Exception e) {
                                                    System.out.println("下载报错:" + href);
                                                    System.out.println("loadFailMessage:" + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }


    public void saveNovel(String href, String title, String content) {
        String fileName = null;
        String filePath = FileUploadUtils.getDefaultBaseDir(); // 保存文件路径
        String exName = "txt"; // 文本
        String random = RandomStringUtils.random(8, false, true);
        String regEx = "[`~!@#$%^&*+=|{}':',\\[\\]./?~！@#￥%……&*——+{}▉◆【】‘；：”“’。， 、？]";
        title = title.replaceAll(regEx, "");
        fileName = DateUtils.dateTime() + href + title + "." + exName; // 文件名称
        try {
            File file = new File(filePath + "/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            // 开始下载小说
            System.out.println("开始下载小说=" + title);
            PrintWriter pw = new PrintWriter(file);
            content = content.replaceAll(" ", "\n");
            pw.write(content);
            pw.close();
            System.out.println(title + "--小说下载完成");
        } catch (IOException e) {
            System.out.println(title + "--小说下载失败");
            e.printStackTrace();
        }
    }

    public void downloadPic(String url, String href, Integer j) {
        try {
            Document catePageDocument = null;
            SSLHelper.init();
            if (j == 1) {
                System.out.println(href + "---j---:" + url + href);
                catePageDocument = Jsoup.connect(url + href).get();
            } else {
                System.out.println(href + "---j---:" + url + href + "index_" + j + ".html");
                catePageDocument = Jsoup.connect(url + href + "index_" + j + ".html").get();
            }

//            log.info("美图***************" + catePageDocument.outerHtml());
            Elements catePageList = catePageDocument.getElementsByAttributeValue("class", "row col6 clearfix");
            Elements hrefList = catePageList.select("a");
            Map<String, String> hrefMap = new HashMap<>();
            for (int k = 0; k < hrefList.size(); k++) {
                // 页面的链接
                Element pageHtml = hrefList.get(k);
                String pageUrl = pageHtml.attributes().get("href");
//                System.out.println(href + "---k---" + url + pageUrl);
                hrefMap.put(pageUrl, href);
            }
            for (String pageUrl : hrefMap.keySet()) {
                // 获取图片的页面
                SSLHelper.init();
                Document imagesDocument = Jsoup.connect(url + pageUrl).get();
//                    log.info("imagesDocument" + imagesDocument);
                Elements nameElements = imagesDocument.getElementsByAttributeValue("class", "main");
                Elements nameElement = nameElements.get(0).select("h1");
//                    log.info("nameElement=====" + nameElement.get(0).text());
                Elements imgElements = imagesDocument.getElementsByAttributeValue("class", "pic");
                Elements imgUrlElements = imgElements.get(0).select("img");
                for (int l = 0; l < imgUrlElements.size(); l++) {
                    Element imgElement = imgUrlElements.get(l);
                    String imgSrc = imgElement.attributes().get("src");
//                        log.info("imgElement=====" + imgElement.attributes().get("src"));
                    this.downLoadimg(imgSrc, href, nameElement.get(0).text(), l);
                }
            }
        } catch (IOException e) {
            System.out.println("download is fail:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void downLoadNovel(String url, String href, Integer j) {
        try {
            Document catePageDocument = null;
            SSLHelper.init();
            if (j == 1) {
                System.out.println(href + "---j---:" + url + href);
                catePageDocument = Jsoup.connect(url + href).get();
            } else {
                System.out.println(href + "---j---:" + url + href + "index_" + j + ".html");
                catePageDocument = Jsoup.connect(url + href + "index_" + j + ".html").get();
            }
//            log.info(catePageDocument.outerHtml());
            Elements catePageList = catePageDocument.getElementsByAttributeValue("class", "row-book clearfix");
            Elements hrefList = catePageList.select("a");
//            log.info("outerHtml========" + hrefList.outerHtml());
            Map<String, String> hrefMap = new HashMap<>();
            for (int k = 0; k < hrefList.size(); k++) {
                // 页面的链接
                Element pageHtml = hrefList.get(k);
                String pageUrl = pageHtml.attributes().get("href");
//                System.out.println(href + "---k---" + url + pageUrl);
                hrefMap.put(pageUrl, href);
            }
            for (String pageUrl : hrefMap.keySet()) {
                // 获取小说的页面
                SSLHelper.init();
                Document novelDocument = Jsoup.connect(url + pageUrl).get();
//                    log.info("novelDocument======" + novelDocument);
                Elements novelElements = novelDocument.getElementsByAttributeValue("class", "main book");
                Elements novelNameElement = novelElements.get(0).select("h1");
                log.info("novelNameElement=====" + novelNameElement.get(0).text());
                Element contentElements = novelElements.get(0);
                String[] conts = contentElements.text().split("加入收藏夹");
//                log.info("contentElements=====" + conts[1]);
                this.saveNovel(href, novelNameElement.get(0).text(), conts[1]);
            }
        } catch (IOException e) {
            System.out.println("download is fail:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 视频解析
     *
     * @param url
     * @param href
     * @param j
     */
    public void downLoad(String url, String href, Integer j) {
        System.out.println("cateType------:" + href);
        try {
            Document catePageDocument = null;
            SSLHelper.init();
            if (j == 1) {
                System.out.println(href + "---j---:" + url + href);
                catePageDocument = Jsoup.connect(url + href).get();
            } else {
                System.out.println(href + "---j---:" + url + href + "index_" + j + ".html");
                catePageDocument = Jsoup.connect(url + href + "index_" + j + ".html").get();
            }
            Elements catePageList = catePageDocument.getElementsByAttributeValue("class", "row col5 clearfix");
            Elements hrefList = catePageList.select("a");
            log.info("videoHref=========" + href);
            Map<String, String> hrefMap = new HashMap<>();
            for (int k = 0; k < hrefList.size(); k++) {
                // 页面的链接
                Element pageHtml = hrefList.get(k);
                String pageUrl = pageHtml.attributes().get("href");
                hrefMap.put(pageUrl, href);
                System.out.println(href + "---k---" + url + pageUrl);
            }
            for (String pageUrl : hrefMap.keySet()) {
                // 获取视频的页面
                SSLHelper.init();
                Document videoDocument = Jsoup.connect(url + pageUrl).get();
                Elements nameElements = videoDocument.getElementsByAttributeValue("class", "textlink");
                Element nameElement = nameElements.get(0);
                String videName = nameElement.childNode(6).outerHtml().replaceAll("&nbsp;&nbsp;»&nbsp;&nbsp;", "");
                Element videoUrlElement = videoDocument.getElementById("url");
                String videoUrl = videoUrlElement.val();
                String fileName = this.downLoadVideo(videoUrl, href, videName);
            }
        } catch (IOException e) {
            System.out.println("download is fail:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 下载方法
     *
     * @param videoUrl
     * @return 文件地址
     */
    public String downLoadVideo(String videoUrl, String href, String videName) {
        String fileName = null;
        HttpURLConnection httpURLConnection = null;
        try {
            //开始时间
            Date begindate = new Date();
            URL uri = new URL(videoUrl);
            httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            httpURLConnection.setRequestProperty("Refere", videoUrl);
            InputStream in = httpURLConnection.getInputStream();
            String filePath = FileUploadUtils.getDefaultBaseDir();
            String exName = FileUploadUtils.getExtName(videoUrl);
            String random = RandomStringUtils.random(8, false, true);
            String regEx = "[`~!@#$%^&*+=|{}':',\\[\\].<>/?~！@#￥%……&*——+{}▉◆【】‘；：”“’。， 、？]";
            videName = videName.replaceAll(regEx, "");
            fileName = DateUtils.dateTime() + href + videName + "." + exName;
            File file = new File(filePath + "/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fo = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int length = 0;
            System.out.println("开始下载videoUrl=:" + videoUrl);
            while ((length = in.read(buf, 0, buf.length)) != -1) {
                fo.write(buf, 0, length);
            }
            in.close();
            fo.close();
            System.out.println("下载完成,服务器文件地址：" + filePath + "/" + fileName);
            //结束时间
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            System.out.println("耗时：" + time / 1000 + "s");
        } catch (IOException e) {
            System.out.println("下载失败videoUrl=" + videoUrl);
            e.printStackTrace();
        }

        return fileName;
    }


    public String downLoadimg(String imgUrl, String href, String title, Integer i) {
        String fileName = null;
        HttpURLConnection httpURLConnection = null;
        try {
            //开始时间
            Date begindate = new Date();
            URL uri = new URL(imgUrl);
            httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            httpURLConnection.setRequestProperty("Refere", imgUrl);
            InputStream in = httpURLConnection.getInputStream();
            String filePath = FileUploadUtils.getDefaultBaseDir();
            String exName = FileUploadUtils.getExtName(imgUrl);
            String random = RandomStringUtils.random(8, false, true);
            String regEx = "[`~!@#$%^&*+=|{}':',\\[\\].<>/?~！@#￥%……&*——+{}▉◆【】‘；：”“’。， 、？]";
            title = title.replaceAll(regEx, "");
            fileName = DateUtils.dateTime() + href + title + "/" + i + "." + exName;
            File file = new File(filePath + "/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fo = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int length = 0;
            System.out.println("开始下载图片:" + imgUrl);
            while ((length = in.read(buf, 0, buf.length)) != -1) {
                fo.write(buf, 0, length);
            }
            in.close();
            fo.close();
            System.out.println(fileName + "---图片下载完成");
            //结束时间
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            System.out.println("耗时：" + time / 1000 + "s");
        } catch (IOException e) {
            System.out.println("图片下载失败imgUrl:" + imgUrl);
            e.printStackTrace();
        }

        return fileName;
    }

    //下载图片
    private void Download(List<String> listImgSrc) {
        HttpURLConnection httpURLConnection = null;
        try {
            //开始时间
            Date begindate = new Date();
            for (String imgUrl : listImgSrc) {
                //开始时间
                Date begindate2 = new Date();
                URL uri = new URL(imgUrl);
                httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                httpURLConnection.setRequestProperty("Refere", imgUrl);
                InputStream in = httpURLConnection.getInputStream();
                String filePath = FileUploadUtils.getDefaultBaseDir();
                String exName = FileUploadUtils.getExtName(imgUrl);
                String random = RandomStringUtils.random(8, false, true);
                String fileName = DateUtils.dateTime() + random + "." + exName;
                File file = new File(filePath + "/" + fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fo = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + imgUrl);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                in.close();
                fo.close();
                System.out.println(fileName + "下载完成");
                //结束时间
                Date overdate2 = new Date();
                double time = overdate2.getTime() - begindate2.getTime();
                System.out.println("耗时：" + time / 1000 + "s");
            }
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            System.out.println("总耗时：" + time / 1000 + "s");
        } catch (Exception e) {
            System.out.println("下载失败:" + e.getMessage());
        }
    }


}

@Data
class Article {
    private String url_list;
    private String url_img;
    private String articlename;
    private String author;
    private String intro;
    private Integer cate_type;
}
