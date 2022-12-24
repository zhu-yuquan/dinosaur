package com.yuquancoco.novel.domain;

import com.yuquancoco.common.annotation.Excel;
import com.yuquancoco.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 小说章节对象 per_catalogue
 *
 * @author zyq
 * @date 2021-08-16
 */
@Data
public class PerCatalogue extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 章节id
     */
    private String catalogueId;

    /**
     * 小说id
     */
    private String novelId;

    /**
     * 章节名称
     */
    @Excel(name = "章节名称")
    private String catalogueName;

    /**
     * 章节编码
     */
    @Excel(name = "章节编码")
    private Integer catalogueCode;

    /**
     * 章节链接地址
     */
    @Excel(name = "章节链接地址")
    private String catalogueUrl;
    /**
     * 章节内容
     */
    @Excel(name = "章节内容")
    private String catalogueText;

    /**
     * 删除标志（N代表存在 Y代表删除）
     */
    private String delFlag;


}
