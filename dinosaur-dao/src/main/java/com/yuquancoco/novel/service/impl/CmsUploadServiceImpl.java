package com.yuquancoco.novel.service.impl;

import java.util.List;
import com.yuquancoco.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuquancoco.novel.mapper.CmsUploadMapper;
import com.yuquancoco.novel.domain.CmsUpload;
import com.yuquancoco.novel.service.ICmsUploadService;

/**
 * 文件Service业务层处理
 *
 * @author zyq
 * @date 2021-08-16
 */
@Service
public class CmsUploadServiceImpl implements ICmsUploadService {
    @Autowired
    private CmsUploadMapper cmsUploadMapper;

    /**
     * 查询文件
     *
     * @param uploadId 文件ID
     * @return 文件
     */
    @Override
    public CmsUpload selectCmsUploadById(Long uploadId) {
        return cmsUploadMapper.selectCmsUploadById(uploadId);
    }

    /**
     * 查询文件列表
     *
     * @param cmsUpload 文件
     * @return 文件
     */
    @Override
    public List<CmsUpload> selectCmsUploadList(CmsUpload cmsUpload) {
        return cmsUploadMapper.selectCmsUploadList(cmsUpload);
    }

    /**
     * 新增文件
     *
     * @param cmsUpload 文件
     * @return 结果
     */
    @Override
    public int insertCmsUpload(CmsUpload cmsUpload) {
        cmsUpload.setCreateTime(DateUtils.getNowDate());
        return cmsUploadMapper.insertCmsUpload(cmsUpload);
    }

    /**
     * 修改文件
     *
     * @param cmsUpload 文件
     * @return 结果
     */
    @Override
    public int updateCmsUpload(CmsUpload cmsUpload) {
        cmsUpload.setUpdateTime(DateUtils.getNowDate());
        return cmsUploadMapper.updateCmsUpload(cmsUpload);
    }

    /**
     * 批量删除文件
     *
     * @param uploadIds 需要删除的文件ID
     * @return 结果
     */
    @Override
    public int deleteCmsUploadByIds(Long[] uploadIds) {
        return cmsUploadMapper.deleteCmsUploadByIds(uploadIds);
    }

    /**
     * 删除文件信息
     *
     * @param uploadId 文件ID
     * @return 结果
     */
    @Override
    public int deleteCmsUploadById(Long uploadId) {
        return cmsUploadMapper.deleteCmsUploadById(uploadId);
    }
}
