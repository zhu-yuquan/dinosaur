package com.yuquancoco.novel.service;

import com.yuquancoco.novel.domain.PerNovel;

import java.util.List;

/**
 * 小说Service接口
 * 
 * @author zyq
 * @date 2021-08-16
 */
public interface WxIPerNovelService {
    /**
     * 查询小说
     * 
     * @param novelId 小说ID
     * @return 小说
     */
    public PerNovel selectPerNovelById(String novelId);

    /**
     * 查询小说列表
     * 
     * @param perNovel 小说
     * @return 小说集合
     */
    public List<PerNovel> selectPerNovelList(PerNovel perNovel);

    /**
     * 新增小说
     * 
     * @param perNovel 小说
     * @return 结果
     */
    public int insertPerNovel(PerNovel perNovel);

    /**
     * 修改小说
     * 
     * @param perNovel 小说
     * @return 结果
     */
    public int updatePerNovel(PerNovel perNovel);

    /**
     * 批量删除小说
     * 
     * @param novelIds 需要删除的小说ID
     * @return 结果
     */
    public int deletePerNovelByIds(String[] novelIds);

    /**
     * 删除小说信息
     * 
     * @param novelId 小说ID
     * @return 结果
     */
    public int deletePerNovelById(String novelId);
}
