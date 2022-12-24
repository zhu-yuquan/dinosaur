package com.yuquancoco.novel.service.impl;

import java.util.List;
import com.yuquancoco.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuquancoco.novel.mapper.PerVideoMapper;
import com.yuquancoco.novel.domain.PerVideo;
import com.yuquancoco.novel.service.IPerVideoService;

/**
 * 视频Service业务层处理
 * 
 * @author zyq
 * @date 2022-02-03
 */
@Service
public class PerVideoServiceImpl implements IPerVideoService 
{
    @Autowired
    private PerVideoMapper perVideoMapper;

    /**
     * 查询视频
     * 
     * @param videoId 视频主键
     * @return 视频
     */
    @Override
    public PerVideo selectPerVideoByVideoId(Long videoId)
    {
        return perVideoMapper.selectPerVideoByVideoId(videoId);
    }

    /**
     * 查询视频列表
     * 
     * @param perVideo 视频
     * @return 视频
     */
    @Override
    public List<PerVideo> selectPerVideoList(PerVideo perVideo)
    {
        return perVideoMapper.selectPerVideoList(perVideo);
    }

    /**
     * 新增视频
     * 
     * @param perVideo 视频
     * @return 结果
     */
    @Override
    public int insertPerVideo(PerVideo perVideo)
    {
        perVideo.setCreateTime(DateUtils.getNowDate());
        return perVideoMapper.insertPerVideo(perVideo);
    }

    /**
     * 修改视频
     * 
     * @param perVideo 视频
     * @return 结果
     */
    @Override
    public int updatePerVideo(PerVideo perVideo)
    {
        perVideo.setUpdateTime(DateUtils.getNowDate());
        return perVideoMapper.updatePerVideo(perVideo);
    }

    /**
     * 批量删除视频
     * 
     * @param videoIds 需要删除的视频主键
     * @return 结果
     */
    @Override
    public int deletePerVideoByVideoIds(Long[] videoIds)
    {
        return perVideoMapper.deletePerVideoByVideoIds(videoIds);
    }

    /**
     * 删除视频信息
     * 
     * @param videoId 视频主键
     * @return 结果
     */
    @Override
    public int deletePerVideoByVideoId(Long videoId)
    {
        return perVideoMapper.deletePerVideoByVideoId(videoId);
    }
}
