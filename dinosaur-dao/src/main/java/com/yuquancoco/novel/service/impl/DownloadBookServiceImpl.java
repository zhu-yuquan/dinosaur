package com.yuquancoco.novel.service.impl;

import com.yuquancoco.common.utils.file.FileUploadUtils;
import com.yuquancoco.novel.domain.*;
import com.yuquancoco.novel.mapper.*;
import com.yuquancoco.novel.service.DownloadBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyq
 * @Title: DownloadBookServiceImpl
 * @Description:
 * @date 2021/8/10 10:35
 */
@Slf4j
@Service
public class DownloadBookServiceImpl implements DownloadBookService {

    @Autowired
    private PerNovelMapper perNovelMapper;
    @Autowired
    private PerCatalogueMapper perCatalogueMapper;
    @Autowired
    private PerPhotoMapper perPhotoMapper;
    @Autowired
    private CmsUploadMapper  cmsUploadMapper;
    @Autowired
    private PerVideoMapper perVideoMapper;

    @Override
    public void getvideo(String url, String href, Integer j) {
        log.info("cateType------:" + href);
        try {
            Document catePageDocument = null;
            SSLHelper.init();
            if (j == 1) {
//                log.info(href + "---j---:" + url + href);
                catePageDocument = Jsoup.connect(url + href).get();
            } else {
//                log.info(href + "---j---:" + url + href + "index_" + j + ".html");
                catePageDocument = Jsoup.connect(url + href + "index_" + j + ".html").get();
            }
            Elements catePageList = catePageDocument.getElementsByAttributeValue("class", "row col5 clearfix");
            Elements hrefList = catePageList.select("a");
//            log.info("videoHref=========" + href);
            Map<String, String> hrefMap = new HashMap<>();
            for (int k = 0; k < hrefList.size(); k++) {
                // 页面的链接
                Element pageHtml = hrefList.get(k);
                String pageUrl = pageHtml.attributes().get("href");
                hrefMap.put(pageUrl, href);
//                log.info(href + "---k---" + url + pageUrl);
            }
            for (String pageUrl : hrefMap.keySet()) {
                // 获取视频的页面
                SSLHelper.init();
//                log.info("pageUrl:" + url + pageUrl);
                Document videoDocument = Jsoup.connect(url + pageUrl).get();
                Elements nameElements = videoDocument.getElementsByAttributeValue("class", "textlink");
                Element nameElement = nameElements.get(0);
                String videName = nameElement.childNode(6).outerHtml().replaceAll("&nbsp;&nbsp;»&nbsp;&nbsp;", "");
                Element videoUrlElement = videoDocument.getElementById("url");
                String videoUrl = videoUrlElement.val();
//                log.info("videoUrl=" + videoUrl + ",href=" + href + ",videName=" + videName);
//                this.downLoadVideo(videoUrl, href, videName);
                this.saveVideo(videoUrl, href, videName);
            }
        } catch (IOException e) {
            log.info("download is fail:" + e.getMessage());
//            e.printStackTrace();
        }
    }

    @Override
    public void saveVideo(String videoUrl, String href, String videName){
        PerVideo perVideo = new PerVideo();
        perVideo.setVideoCode(href);
        perVideo.setVideoName(videName);
        perVideo.setVideoUrl(videoUrl);
        perVideoMapper.insertPerVideo(perVideo);
    }

    /**
     * 下载方法
     *
     * @param videoUrl
     * @return 文件地址
     */
    @Override
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
            String regEx = "[`~!@#$%^&*+=|{}':',\\[\\].<>/?~！@#￥%……&*——+{}▉●◆【】‘；：”“’。， 、？]";
            videName = videName.replaceAll(regEx, "");
            fileName = href + videName + "." + exName;
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
            log.info("开始下载videoUrl=:" + videoUrl);
            while ((length = in.read(buf, 0, buf.length)) != -1) {
                fo.write(buf, 0, length);
            }
            in.close();
            fo.close();
            log.info("下载完成,服务器文件地址：" + filePath + "/" + fileName);
            //结束时间
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            log.info("耗时：" + time / 1000 + "s");
        } catch (IOException e) {
            log.info("下载失败videoUrl=" + videoUrl);
//            e.printStackTrace();
        }

        return fileName;
    }

    @Override
    public void downloadPic(String url, String href, Integer j) {
        try {
            Document catePageDocument = null;
            SSLHelper.init();
            if (j == 1) {
                catePageDocument = Jsoup.connect(url + href).get();
            } else {
                catePageDocument = Jsoup.connect(url + href + "index_" + j + ".html").get();
            }
            Elements catePageList = catePageDocument.getElementsByAttributeValue("class", "row col6 clearfix");
            Elements hrefList = catePageList.select("a");
            Map<String, String> hrefMap = new HashMap<>();
            for (int k = 0; k < hrefList.size(); k++) {
                // 页面的链接
                Element pageHtml = hrefList.get(k);
                String pageUrl = pageHtml.attributes().get("href");
                hrefMap.put(pageUrl, href);
            }
            for (String pageUrl : hrefMap.keySet()) {
                // 获取图片的页面
                SSLHelper.init();
                Document imagesDocument = Jsoup.connect(url + pageUrl).get();
                Elements nameElements = imagesDocument.getElementsByAttributeValue("class", "main");
                Elements nameElement = nameElements.get(0).select("h1");
                Elements imgElements = imagesDocument.getElementsByAttributeValue("class", "pic");
                Elements imgUrlElements = imgElements.get(0).select("img");
                for (int l = 0; l < imgUrlElements.size(); l++) {
                    Element imgElement = imgUrlElements.get(l);
                    String imgSrc = imgElement.attributes().get("src");
                    this.picInsert(imgSrc, href, nameElement.get(0).text(), l);
                    this.downLoadimg(imgSrc, href, nameElement.get(0).text(), l);
                }
            }
        } catch (IOException e) {
            log.info("download is fail:" + e.getMessage());
//            e.printStackTrace();
        }
    }

    /**
     * 图片下载
     * @param imgUrl
     * @param href
     * @param title
     * @param i
     * @return
     */
    @Override
    public void picInsert(String imgUrl, String href, String title, Integer i){
        String regEx = "[`~!@#$%^&*+=|{}':',./?~！@#￥%……&*——+{}▉●◆【】‘；：”“’。， 、？]";
        title = title.replaceAll(regEx, "");
        PerPhoto perPhoto = new PerPhoto();
        perPhoto.setPhotoName(title);
        perPhoto.setDelFlag("N");
        perPhoto.setUserId(6666l);
        List<PerPhoto> perPhotoList = perPhotoMapper.selectPerPhotoList(perPhoto);
        if (perPhotoList.size() == 0){
            perPhoto.setRemark(href);
            perPhotoMapper.insertPerPhoto(perPhoto);
        }else {
            perPhoto = perPhotoList.get(0);
        }
        CmsUpload cmsUpload = new CmsUpload();
        cmsUpload.setAbsolutePath(imgUrl);
        cmsUpload.setBizType(FileUploadUtils.getExtName(imgUrl));
        cmsUpload.setType(FileUploadUtils.getExtName(imgUrl));
        cmsUpload.setNewFileName(i + "-" + href);
        cmsUpload.setOldFileName(title + i);
        cmsUpload.setOwnerId(perPhoto.getPhotoId());
        cmsUpload.setOwnerType("PerPhoto");
        cmsUpload.setSize(66l);
        cmsUpload.setDelFlag("N");
        List<CmsUpload> cmsUploadList = cmsUploadMapper.selectCmsUploadList(cmsUpload);
        if (cmsUploadList.size() == 0){
            cmsUpload.setSeq(i.longValue());
            cmsUpload.setUploadTime(new Date());
            cmsUploadMapper.insertCmsUpload(cmsUpload);
        }

    }

    /**
     * 图片下载
     * @param imgUrl
     * @param href
     * @param title
     * @param i
     * @return
     */
    @Override
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
            String regEx = "[`~!@#$%^&*+=|{}':',\\[\\].<>/?~！@#￥%……&*——+{}▉●◆【】‘；：”“’。， 、？]";
            title = title.replaceAll(regEx, "").trim();
            fileName = href + title + "/"+ i + "." + exName;
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
//            log.info("开始下载图片:" + imgUrl);
            while ((length = in.read(buf, 0, buf.length)) != -1) {
                fo.write(buf, 0, length);
            }
            in.close();
            fo.close();
//            log.info(fileName + "---图片下载完成");
            //结束时间
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
//            log.info("耗时：" + time / 1000 + "s");
        } catch (IOException e) {
            log.info(href + "图片"+title+"下载失败imgUrl:" + imgUrl);
//            e.printStackTrace();
        }

        return fileName;
    }

    /**
     * 小说
     * @param url
     * @param href
     * @param j
     */
    @Override
    public void downLoadNovel(String url, String href, Integer j) {
        try {
            Document catePageDocument = null;
            SSLHelper.init();
            if (j == 1) {
//                log.info(href + "---j---:" + url + href);
                catePageDocument = Jsoup.connect(url + href).get();
            } else {
//                log.info(href + "---j---:" + url + href + "index_" + j + ".html");
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
//                log.info(href + "---k---" + url + pageUrl);
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
//                this.insertNovel(href, novelNameElement.get(0).text(), conts[1]);
                this.saveNovel(href, novelNameElement.get(0).text(), conts[1]);
            }
        } catch (IOException e) {
            log.info("download is fail:" + e.getMessage());
//            e.printStackTrace();
        }
    }

    @Override
    public void insertNovel(String href, String title, String content) {
        String regEx = "[`~!@#$%^&*+=|{}':',.<>/?~！@#￥%……&*——+{}▉●◆【】‘；：”“’。， 、？]";
        title = title.replaceAll(regEx, "");
        PerCatalogue perCatalogue = new PerCatalogue();
        perCatalogue.setNovelId("66666666");
        perCatalogue.setDelFlag("N");
        perCatalogue.setCatalogueUrl(href);
        perCatalogue.setCatalogueName(title);
        perCatalogue.setCatalogueText(content);
        List<PerCatalogue> perCatalogueList = perCatalogueMapper.selectPerCatalogueList(perCatalogue);
        if (perCatalogueList.size() == 0){
            perCatalogue.setCatalogueCode(RandomUtils.nextInt(10000, 999999999));
            perCatalogueMapper.insertPerCatalogue(perCatalogue);
        }

    }

    @Override
    public void saveNovel(String href, String title, String content) {
        String fileName = null;
        String filePath = FileUploadUtils.getDefaultBaseDir(); // 保存文件路径
        String exName = "txt"; // 文本
        String random = RandomStringUtils.random(8, false, true);
        String regEx = "[`~!@#$%^&*+=|{}':',\\[\\]./?~！@#￥%……&*——+{}▉●◆【】‘；：”“’。， 、？]";
        title = title.replaceAll(regEx, "");
        fileName = href + title + "." + exName; // 文件名称
        try {
            File file = new File(filePath + "/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            // 开始下载小说
            log.info("开始下载小说=" + title);
            PrintWriter pw = new PrintWriter(file);
            content = content.replaceAll(" ", "\n");
            pw.write(content);
            pw.close();
            log.info(title + "--小说下载完成");
        } catch (IOException e) {
            log.info(title + "--小说下载失败");
//            e.printStackTrace();
        }
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
            log.info("save error message fail：" + e.getMessage());
        }
    }

}
