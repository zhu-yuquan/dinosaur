package com.yuquancoco.novel.service;

import java.util.List;
import com.yuquancoco.novel.domain.PerPhoto;

/**
 * 相册Service接口
 * 
 * @author zyq
 * @date 2021-08-16
 */
public interface IPerPhotoService {
    /**
     * 查询相册
     * 
     * @param photoId 相册ID
     * @return 相册
     */
    public PerPhoto selectPerPhotoById(Long photoId);

    /**
     * 查询相册列表
     * 
     * @param perPhoto 相册
     * @return 相册集合
     */
    public List<PerPhoto> selectPerPhotoList(PerPhoto perPhoto);

    /**
     * 新增相册
     * 
     * @param perPhoto 相册
     * @return 结果
     */
    public int insertPerPhoto(PerPhoto perPhoto);

    /**
     * 修改相册
     * 
     * @param perPhoto 相册
     * @return 结果
     */
    public int updatePerPhoto(PerPhoto perPhoto);

    /**
     * 批量删除相册
     * 
     * @param photoIds 需要删除的相册ID
     * @return 结果
     */
    public int deletePerPhotoByIds(Long[] photoIds);

    /**
     * 删除相册信息
     * 
     * @param photoId 相册ID
     * @return 结果
     */
    public int deletePerPhotoById(Long photoId);
}
