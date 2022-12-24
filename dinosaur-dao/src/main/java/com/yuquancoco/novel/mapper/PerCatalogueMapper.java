package com.yuquancoco.novel.mapper;

import java.util.List;
import com.yuquancoco.novel.domain.PerCatalogue;

/**
 * 小说章节Mapper接口
 * 
 * @author zyq
 * @date 2021-08-16
 */
public interface PerCatalogueMapper {
    /**
     * 查询小说章节
     * 
     * @param catalogueId 小说章节ID
     * @return 小说章节
     */
    public PerCatalogue selectPerCatalogueById(String catalogueId);

    /**
     * 查询小说章节列表
     * 
     * @param perCatalogue 小说章节
     * @return 小说章节集合
     */
    public List<PerCatalogue> selectPerCatalogueList(PerCatalogue perCatalogue);

    /**
     * 新增小说章节
     * 
     * @param perCatalogue 小说章节
     * @return 结果
     */
    public int insertPerCatalogue(PerCatalogue perCatalogue);

    /**
     * 修改小说章节
     * 
     * @param perCatalogue 小说章节
     * @return 结果
     */
    public int updatePerCatalogue(PerCatalogue perCatalogue);

    /**
     * 删除小说章节
     * 
     * @param catalogueId 小说章节ID
     * @return 结果
     */
    public int deletePerCatalogueById(String catalogueId);

    /**
     * 批量删除小说章节
     * 
     * @param catalogueIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePerCatalogueByIds(String[] catalogueIds);
}
