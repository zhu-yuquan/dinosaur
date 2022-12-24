package com.yuquancoco.novel.domain;

import com.yuquancoco.common.annotation.Excel;
import com.yuquancoco.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 视频对象 per_video
 *
 * @author zyq
 * @date 2022-02-03
 */
@Data
public class PerVideo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 视频id */
    private Long videoId;

    /** 视频名 */
    @Excel(name = "视频名")
    private String videoName;

    /** 作者 */
    @Excel(name = "作者")
    private String videoAuthor;

    /** 视频code */
    @Excel(name = "视频code")
    private String videoCode;

    /** 视频状态 */
    @Excel(name = "视频状态")
    private String videoStatus;

    /** 视频封面图片 */
    @Excel(name = "视频封面图片")
    private String videoImage;

    /** 视频链接地址 */
    @Excel(name = "视频链接地址")
    private String videoUrl;

    /** 视频简介 */
    @Excel(name = "视频简介")
    private String videoIntro;

    /** 视频分类 */
    @Excel(name = "视频分类")
    private String cateStr;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String cateName;

    /** 删除标志（N代表存在 Y代表删除） */
    private String delFlag;


}
