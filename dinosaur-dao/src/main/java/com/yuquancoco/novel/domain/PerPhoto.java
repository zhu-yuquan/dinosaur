package com.yuquancoco.novel.domain;

import com.yuquancoco.common.annotation.Excel;
import com.yuquancoco.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 相册对象 per_photo
 *
 * @author zyq
 * @date 2021-08-16
 */
@Data
public class PerPhoto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 相册id
     */
    private Long photoId;

    /**
     * 父级id
     */
    @Excel(name = "父级id")
    private Long parentId;

    /**
     * 相册所属人id
     */
    @Excel(name = "相册所属人id")
    private Long userId;

    /**
     * 相册名称
     */
    @Excel(name = "相册名称")
    private String photoName;

    /**
     * 是否公开(0=公开,1=朋友可见,3=私人可见)
     */
    @Excel(name = "是否公开", readConverterExp = "0=公开,1=朋友可见,3=私人可见")
    private Long visibleType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（N代表存在 Y代表删除）
     */
    private String delFlag;


}
