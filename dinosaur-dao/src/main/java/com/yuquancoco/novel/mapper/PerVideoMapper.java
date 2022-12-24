package com.yuquancoco.novel.mapper;

import java.util.List;
import com.yuquancoco.novel.domain.PerVideo;

/**
 * 视频Mapper接口
 *
 * @author zyq
 * @date 2022-02-03
 */
public interface PerVideoMapper
{
    /**
     * 查询视频
     *
     * @param videoId 视频主键
     * @return 视频
     */
    public PerVideo selectPerVideoByVideoId(Long videoId);

    /**
     * 查询视频列表
     *
     * @param perVideo 视频
     * @return 视频集合
     */
    public List<PerVideo> selectPerVideoList(PerVideo perVideo);

    /**
     * 新增视频
     *
     * @param perVideo 视频
     * @return 结果
     */
    public int insertPerVideo(PerVideo perVideo);

    /**
     * 修改视频
     *
     * @param perVideo 视频
     * @return 结果
     */
    public int updatePerVideo(PerVideo perVideo);

    /**
     * 删除视频
     *
     * @param videoId 视频主键
     * @return 结果
     */
    public int deletePerVideoByVideoId(Long videoId);

    /**
     * 批量删除视频
     *
     * @param videoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePerVideoByVideoIds(Long[] videoIds);
}
