package com.yuquancoco.novel.service.impl;

import java.util.List;
import com.yuquancoco.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuquancoco.novel.mapper.PerCatalogueMapper;
import com.yuquancoco.novel.domain.PerCatalogue;
import com.yuquancoco.novel.service.IPerCatalogueService;

/**
 * 小说章节Service业务层处理
 *
 * @author zyq
 * @date 2021-08-16
 */
@Service
public class PerCatalogueServiceImpl implements IPerCatalogueService {
    @Autowired
    private PerCatalogueMapper perCatalogueMapper;

    /**
     * 查询小说章节
     *
     * @param catalogueId 小说章节ID
     * @return 小说章节
     */
    @Override
    public PerCatalogue selectPerCatalogueById(String catalogueId) {
        return perCatalogueMapper.selectPerCatalogueById(catalogueId);
    }

    /**
     * 查询小说章节列表
     *
     * @param perCatalogue 小说章节
     * @return 小说章节
     */
    @Override
    public List<PerCatalogue> selectPerCatalogueList(PerCatalogue perCatalogue) {
        return perCatalogueMapper.selectPerCatalogueList(perCatalogue);
    }

    /**
     * 新增小说章节
     *
     * @param perCatalogue 小说章节
     * @return 结果
     */
    @Override
    public int insertPerCatalogue(PerCatalogue perCatalogue) {
        perCatalogue.setCreateTime(DateUtils.getNowDate());
        return perCatalogueMapper.insertPerCatalogue(perCatalogue);
    }

    /**
     * 修改小说章节
     *
     * @param perCatalogue 小说章节
     * @return 结果
     */
    @Override
    public int updatePerCatalogue(PerCatalogue perCatalogue) {
        perCatalogue.setUpdateTime(DateUtils.getNowDate());
        return perCatalogueMapper.updatePerCatalogue(perCatalogue);
    }

    /**
     * 批量删除小说章节
     *
     * @param catalogueIds 需要删除的小说章节ID
     * @return 结果
     */
    @Override
    public int deletePerCatalogueByIds(String[] catalogueIds) {
        return perCatalogueMapper.deletePerCatalogueByIds(catalogueIds);
    }

    /**
     * 删除小说章节信息
     *
     * @param catalogueId 小说章节ID
     * @return 结果
     */
    @Override
    public int deletePerCatalogueById(String catalogueId) {
        return perCatalogueMapper.deletePerCatalogueById(catalogueId);
    }
}
