package com.yuquancoco.novel.controller;

import com.yuquancoco.common.utils.StringUtils;
import com.yuquancoco.common.utils.http.HttpUtils;
import com.yuquancoco.novel.domain.SSLHelper;
import com.yuquancoco.novel.service.DownloadBookService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * reptile
 */
@RestController
@Slf4j
public class ReptileDkcwlController {
    @Autowired
    private DownloadBookService downloadBookService;

    @GetMapping("/reptile-dkcwl-download")
    public void getDkcwlList() {
//        reptileDkcwlList();
        // 小说
//        getBookList("都市激情", "/book/type/25/1.html", 130);
//        getBookList("古典仙侠", "/book/type/26/1.html", 126);
//        getBookList("不伦恋情", "/book/type/27/1.html", 195);
//        getBookList("校园春色", "/book/type/28/1.html", 131);
//        getBookList("真实体验", "/book/type/29/1.html", 154);
//        getBookList("科学幻想", "/book/type/30/1.html", 73);
//        getBookList("职场丽人", "/book/type/31/1.html", 37);
//        getBookList("魅力人妻", "/book/type/32/1.html", 109);
        // 图片
//        getImgList("唯美清纯", "/pic/type/17/1.html", 148);
//        getImgList("欧美美图", "/pic/type/18/1.html", 148);
//        getImgList("美腿丝袜", "/pic/type/19/1.html", 149);
//        getImgList("网友自拍", "/pic/type/20/1.html", 117);
//        getImgList("清纯诱惑", "/pic/type/21/1.html", 143);
        getImgList("露出激情", "/pic/type/22/1.html", 97); //97
//        getImgList("卡通漫画", "/pic/type/23/1.html", 102);
//        getImgList("Gif动图", "/pic/type/24/1.html", 139);


    }


    public void reptileDkcwlList() {
        String url = "http://www.dkcwl.com";
        // http://www.dkcwl.com/vod/type/1/1.html
        // http://www.dkcwl.com/vod/type/2/1.html
        // http://www.dkcwl.com/pic/type/19/1.html
        // http://www.dkcwl.com/book/type/27/1.html

        try {
            SSLHelper.init();
            Document document = Jsoup.connect(url).get();
            Elements cateTypeList = document.getElementsByAttributeValue("class", "vr1kq1 ul");
            Elements imglist = cateTypeList.select("a");
            System.out.println("----------------------------------------");
            System.out.println(imglist.outerHtml());
            final Executor executor = Executors.newCachedThreadPool();//启用多线程
            for (int i = 0; i < imglist.size(); i++) {
                Element element = imglist.get(i);
                String href = element.attributes().get("href");
                String title = element.text();

//                System.out.println("-------------------" + i + "---------------------");
//                System.out.println(href + "**********" + title);
                if (StringUtils.isNotEmpty(href) && !"javascript:;".equals(href)) {
                    //电影
                    if (href.indexOf("/vod") != -1 && 2 < 1) {
                        System.out.println("-------------------" + i + "---------------------");
                        System.out.println(href + "**********" + title);
                        SSLHelper.init();
                        Document pageDocument = Jsoup.connect(url + href).get();
//                        System.out.println(pageDocument.outerHtml());

                        Elements lastPageNum = pageDocument.getElementsByTag("section");
                        Elements divPageNum = lastPageNum.select("div");
//                        System.out.println(divPageNum.outerHtml());
                        Elements aPageNum = divPageNum.last().select("a");
                        Element pageEle = aPageNum.get(2);
                        System.out.println("********" + title + "*************" + i + "*********************");
                        System.out.println("lastPageNum=" + pageEle.text());
                        Integer totalPageNum = Integer.valueOf(pageEle.text().replaceAll("共", "").replaceAll("页", "").trim());
                        System.out.println("totalPageNum=" + totalPageNum);
//                        getVodList(title, href,totalPageNum);
                    }
                    //图片
                    if (href.indexOf("/pic") != -1) {
                        System.out.println("-------------------" + i + "---------------------");
                        System.out.println(href + "**********" + title);
                        if (2 > 1) {
                            SSLHelper.init();
                            Document pageDocument = Jsoup.connect(url + href).get();
//                            System.out.println(pageDocument.outerHtml());

                            Elements lastPageNum = pageDocument.getElementsByTag("section");
                            Elements divPageNum = lastPageNum.select("div");
                            Elements aPageNum = divPageNum.get(1).select("a");
                            Element pageEle = aPageNum.get(2);
                            System.out.println("********" + title + "*************" + i + "*********************");
                            System.out.println("lastPageNum=" + pageEle.text());
                            Integer totalPageNum = Integer.valueOf(pageEle.text().replaceAll("共", "").replaceAll("页", "").trim());
                            System.out.println("totalPageNum=" + totalPageNum);
//
//                            executor.execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        // to do
//                                        getImgList(title, href, totalPageNum);
//                                    } catch (Exception e) {
//                                        System.out.println("下载报错:" + href);
//                                        System.out.println("loadFailMessage:" + e.getMessage());
//                                    }
//                                }
//                            });
                        }
                    }
                    //小说
                    if (href.indexOf("/book") != -1) {
                        System.out.println("-------------------" + i + "---------------------");
                        System.out.println(href + "**********" + title);
                        if (2 > 1) {

                            SSLHelper.init();
                            Document pageDocument = Jsoup.connect(url + href).get();
//                            System.out.println(pageDocument.outerHtml());
                            Elements lastPageNum = pageDocument.getElementsByTag("section");
                            Elements divPageNum = lastPageNum.select("div");
                            Elements aPageNum = divPageNum.get(1).select("a");
                            Element pageEle = aPageNum.get(2);
                            System.out.println("********" + title + "*************" + i + "*********************");
                            System.out.println("lastPageNum=" + pageEle.text());
                            Integer totalPageNum = Integer.valueOf(pageEle.text().replaceAll("共", "").replaceAll("页", "").trim());
                            System.out.println("totalPageNum=" + totalPageNum);
//                            executor.execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        // to do
//                                        getBookList(title, href, totalPageNum);
//                                    } catch (Exception e) {
//                                        System.out.println("下载报错:" + href);
//                                        System.out.println("loadFailMessage:" + e.getMessage());
//                                    }
//                                }
//                            });

                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getImgList(String title, String href, Integer totalPageNum) {
        String url = "http://www.dkcwl.com";
        href = StringUtils.substring(href, 0, 13);
        final Executor executor = Executors.newCachedThreadPool();//启用多线程
        for (int j = 1; j <= totalPageNum; j++) {
            try {
                SSLHelper.init();
                String pageUrl = url + href + j + ".html";
                Document pageDocument = Jsoup.connect(pageUrl).get();
//                System.out.println("pagelist:" + pageDocument.outerHtml());
                Elements linkMain = pageDocument.getElementsByTag("main");
                Elements linkUl = linkMain.select("ul");
                Elements linklist = linkUl.select("a");
//                System.out.println("pagelist:" + linklist.outerHtml());
                for (int i = 0; i < linklist.size(); i++) {
                    Element element = linklist.get(i);
                    String pagehref = element.attributes().get("href");
                    String pagetitle = element.attributes().get("title");
//                    System.out.println("*********" + pagehref +"**********"+pagetitle);
                    SSLHelper.init();
                    Document viewDocument = Jsoup.connect(url + pagehref).get();
//                    System.out.println("viewDocument:" + viewDocument.outerHtml());
                    Elements linkimg = viewDocument.getElementsByTag("main");
                    Elements linkImgList = linkimg.select("img");
                    for (int k = 0; k < linkImgList.size(); k++) {
                        Element elemt = linkImgList.get(k);
                        String imgSrc = elemt.attributes().get("src");
                        System.out.println("imgSrc*********" + imgSrc);
//                        downloadBookService.picInsert(imgSrc, href + title + "/", pagetitle, k + 1);
                        downloadBookService.downLoadimg(imgSrc, href + title + "/", pagetitle, k + 1);
                        /*final int ss = k;
                        String pathhref = href + title + "/";
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // to do
                                    downloadBookService.downLoadimg(imgSrc, pathhref, pagetitle, ss + 1);
                                } catch (Exception e) {
                                    System.out.println("下载报错:" + pathhref);
                                    System.out.println("loadFailMessage:" + e.getMessage());
                                }
                            }
                        });*/
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getVodList(String title, String href, Integer totalPageNum) {
        String url = "http://www.dkcwl.com";
        href = StringUtils.substringBeforeLast(href, "/") + "/";
        for (int j = 1; j <= totalPageNum; j++) {
            try {
                SSLHelper.init();
                String pageUrl = url + href + j + ".html";
                Document pageDocument = Jsoup.connect(pageUrl).get();
                System.out.println("pagelist:" + pageDocument.outerHtml());
                Elements linkMain = pageDocument.getElementsByTag("main");
                Elements linkUl = linkMain.select("div");
                Elements linklist = linkUl.first().select("a");
                System.out.println("pagelist:" + linklist.outerHtml());
                for (int i = 0; i < linklist.size(); i++) {
                    Element element = linklist.get(i);
                    String pagehref = element.attributes().get("href");
                    String pagetitle = element.attributes().get("title");
//                    System.out.println("*********" + pagehref +"**********"+pagetitle);
                    SSLHelper.init();
                    Document viewDocument = Jsoup.connect(url + pagehref).get();
                    System.out.println("viewDocument:" + viewDocument.outerHtml());

                    Elements linkimg = viewDocument.getElementsByTag("main");
                    Elements linkImgList = linkimg.select("img");
                    for (int k = 0; k < linkImgList.size(); k++) {
                        Element elemt = linkImgList.get(k);
                        String imgSrc = elemt.attributes().get("src");

//                        System.out.println("imgSrc*********" + imgSrc);
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getBookList(String title, String href, Integer totalPageNum) {
        String url = "http://www.dkcwl.com";
        href = StringUtils.substring(href, 0, 14);
        final Executor executor = Executors.newCachedThreadPool();//启用多线程
        for (int j = 1; j <= totalPageNum; j++) {
            try {
                SSLHelper.init();
                String pageUrl = url + href + j + ".html";
                Document pageDocument = Jsoup.connect(pageUrl).get();
//                System.out.println("pagelist:" + pageDocument.outerHtml());
                Elements linkMain = pageDocument.getElementsByTag("main");
                Elements linkUl = linkMain.select("ul");
                Elements linklist = linkUl.select("a");
                System.out.println("pagelist:" + linklist.outerHtml());
                for (int i = 0; i < linklist.size(); i++) {
                    Element element = linklist.get(i);
                    String pagehref = element.attributes().get("href");
                    String pagetitle = element.attributes().get("title");
                    System.out.println("*********" + pagehref + "**********" + pagetitle);
                    SSLHelper.init();
                    Document viewDocument = Jsoup.connect(url + pagehref).get();
//                    System.out.println("viewDocument:" + viewDocument.outerHtml());

                    Elements linkbook = viewDocument.getElementsByTag("section");
                    Elements linkbookview = linkbook.get(0).select("h2");
                    String bookName = linkbookview.text().trim();
                    System.out.println("bookName=******" + bookName);
                    Elements linkbookcontent = linkbook.get(0).select("div");
//                    System.out.println("bookContent=******" + linkbookcontent.text());
                    String content = linkbookcontent.text();
                    String pathhref = href + title + "/";
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // to do
                                downloadBookService.saveNovel(pathhref, bookName, content);
                            } catch (Exception e) {
                                System.out.println("下载报错:" + pathhref);
                                System.out.println("loadFailMessage:" + e.getMessage());
                            }
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
