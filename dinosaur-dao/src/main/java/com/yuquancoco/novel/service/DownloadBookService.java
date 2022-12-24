package com.yuquancoco.novel.service;

/**
 * @author zyq
 * @Title: DownloadBook
 * @Description:
 * @date 2021/8/10 10:32
 */
public interface DownloadBookService {

    public void getvideo(String url, String href, Integer j);

    public void downloadPic(String url, String href, Integer j);

    public void downLoadNovel(String url, String href, Integer j);

    /**
     * 图片下载
     * @param imgUrl
     * @param href
     * @param title
     * @param i
     * @return
     */
    public void picInsert(String imgUrl, String href, String title, Integer i);

    /**
     * 图片下载
     * @param imgUrl
     * @param href
     * @param title
     * @param i
     * @return
     */
    public String downLoadimg(String imgUrl, String href, String title, Integer i);

    /**
     * 下载小说
     * @param href
     * @param title
     * @param content
     */
    public void saveNovel(String href, String title, String content);

    /**
     * 保存小说到数据库
     * @param href
     * @param title
     * @param content
     */
    public void insertNovel(String href, String title, String content);

    /**
     * 下载方法
     *
     * @param videoUrl
     * @return 文件地址
     */
    public String downLoadVideo(String videoUrl, String href, String videName);

    public void saveVideo(String videoUrl, String href, String videName);

}
