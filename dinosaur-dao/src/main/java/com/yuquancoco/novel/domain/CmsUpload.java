package com.yuquancoco.novel.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuquancoco.common.annotation.Excel;
import com.yuquancoco.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 文件对象 cms_upload
 *
 * @author zyq
 * @date 2021-08-16
 */
@Data
public class CmsUpload extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    private Long uploadId;

    /**
     * 文件绝对路径
     */
    @Excel(name = "文件绝对路径")
    private String absolutePath;

    /**
     * 业务分类
     */
    @Excel(name = "业务分类")
    private String bizType;

    /**
     * 原文件名称
     */
    @Excel(name = "原文件名称")
    private String oldFileName;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private Long latitude;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private Long longitude;

    /**
     * 新文件名称
     */
    @Excel(name = "新文件名称")
    private String newFileName;

    /**
     * 所属Id
     */
    @Excel(name = "所属Id")
    private Long ownerId;

    /**
     * 所属类型
     */
    @Excel(name = "所属类型")
    private String ownerType;

    /**
     * 文件大小
     */
    @Excel(name = "文件大小")
    private Long size;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long seq;

    /**
     * 文件类型
     */
    @Excel(name = "文件类型")
    private String type;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    /**
     * 删除标志（N代表存在 Y代表删除）
     */
    private String delFlag;

}
