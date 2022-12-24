package com.yuquancoco.novel.domain;

import com.yuquancoco.common.annotation.Excel;
import com.yuquancoco.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 小说对象 per_novel
 *
 * @author zyq
 * @date 2021-08-16
 */
@Data
public class PerNovel extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 书名
     */
    @Excel(name = "书名")
    private String bookName;

    /**
     * 作者
     */
    @Excel(name = "作者")
    private String bookAuthor;

    /**
     * 书籍code
     */
    @Excel(name = "书籍code")
    private String bookCode;

    /**
     * 书籍状态(完本、连载)
     */
    @Excel(name = "书籍状态")
    private String bookStatus;

    /**
     * 书籍图片
     */
    @Excel(name = "书籍图片")
    private String bookImage;

    /**
     * 书籍分类
     * (0=完本,1=玄幻,2=武侠,3=都市,4=历史,5=网游,6=科幻,7=女生)
     */
    @Excel(name = "书籍分类", readConverterExp = "0=完本,1=玄幻,2=武侠,3=都市,4=历史,5=网游,6=科幻,7=女生")
    private Integer cateType;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String cateName;

    /**
     * 书籍简介
     */
    @Excel(name = "书籍简介")
    private String bookIntro;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


}
