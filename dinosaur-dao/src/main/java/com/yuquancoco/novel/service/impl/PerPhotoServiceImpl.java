package com.yuquancoco.novel.service.impl;

import java.util.List;
import com.yuquancoco.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuquancoco.novel.mapper.PerPhotoMapper;
import com.yuquancoco.novel.domain.PerPhoto;
import com.yuquancoco.novel.service.IPerPhotoService;

/**
 * 相册Service业务层处理
 *
 * @author zyq
 * @date 2021-08-16
 */
@Service
public class PerPhotoServiceImpl implements IPerPhotoService {
    @Autowired
    private PerPhotoMapper perPhotoMapper;

    /**
     * 查询相册
     *
     * @param photoId 相册ID
     * @return 相册
     */
    @Override
    public PerPhoto selectPerPhotoById(Long photoId) {
        return perPhotoMapper.selectPerPhotoById(photoId);
    }

    /**
     * 查询相册列表
     *
     * @param perPhoto 相册
     * @return 相册
     */
    @Override
    public List<PerPhoto> selectPerPhotoList(PerPhoto perPhoto) {
        return perPhotoMapper.selectPerPhotoList(perPhoto);
    }

    /**
     * 新增相册
     *
     * @param perPhoto 相册
     * @return 结果
     */
    @Override
    public int insertPerPhoto(PerPhoto perPhoto) {
        perPhoto.setCreateTime(DateUtils.getNowDate());
        return perPhotoMapper.insertPerPhoto(perPhoto);
    }

    /**
     * 修改相册
     *
     * @param perPhoto 相册
     * @return 结果
     */
    @Override
    public int updatePerPhoto(PerPhoto perPhoto) {
        perPhoto.setUpdateTime(DateUtils.getNowDate());
        return perPhotoMapper.updatePerPhoto(perPhoto);
    }

    /**
     * 批量删除相册
     *
     * @param photoIds 需要删除的相册ID
     * @return 结果
     */
    @Override
    public int deletePerPhotoByIds(Long[] photoIds) {
        return perPhotoMapper.deletePerPhotoByIds(photoIds);
    }

    /**
     * 删除相册信息
     *
     * @param photoId 相册ID
     * @return 结果
     */
    @Override
    public int deletePerPhotoById(Long photoId) {
        return perPhotoMapper.deletePerPhotoById(photoId);
    }
}
