package com.yuquancoco.novel.service.impl;

import com.yuquancoco.common.utils.DateUtils;
import com.yuquancoco.novel.domain.PerNovel;
import com.yuquancoco.novel.mapper.PerNovelMapper;
import com.yuquancoco.novel.service.WxIPerNovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小说Service业务层处理
 *
 * @author zyq
 * @date 2021-08-16
 */
@Service
public class WxPerNovelServiceImpl implements WxIPerNovelService {
    @Autowired
    private PerNovelMapper perNovelMapper;

    /**
     * 查询小说
     *
     * @param novelId 小说ID
     * @return 小说
     */
    @Override
    public PerNovel selectPerNovelById(String novelId) {
        return perNovelMapper.selectPerNovelById(novelId);
    }

    /**
     * 查询小说列表
     *
     * @param perNovel 小说
     * @return 小说
     */
    @Override
    public List<PerNovel> selectPerNovelList(PerNovel perNovel) {
        return perNovelMapper.selectPerNovelList(perNovel);
    }

    /**
     * 新增小说
     *
     * @param perNovel 小说
     * @return 结果
     */
    @Override
    public int insertPerNovel(PerNovel perNovel) {
        perNovel.setCreateTime(DateUtils.getNowDate());
        return perNovelMapper.insertPerNovel(perNovel);
    }

    /**
     * 修改小说
     *
     * @param perNovel 小说
     * @return 结果
     */
    @Override
    public int updatePerNovel(PerNovel perNovel) {
        perNovel.setUpdateTime(DateUtils.getNowDate());
        return perNovelMapper.updatePerNovel(perNovel);
    }

    /**
     * 批量删除小说
     *
     * @param novelIds 需要删除的小说ID
     * @return 结果
     */
    @Override
    public int deletePerNovelByIds(String[] novelIds) {
        return perNovelMapper.deletePerNovelByIds(novelIds);
    }

    /**
     * 删除小说信息
     *
     * @param novelId 小说ID
     * @return 结果
     */
    @Override
    public int deletePerNovelById(String novelId) {
        return perNovelMapper.deletePerNovelById(novelId);
    }
}
