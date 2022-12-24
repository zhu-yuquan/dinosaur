package com.yuquancoco.novel.mapper;

import java.util.List;
import com.yuquancoco.novel.domain.CmsUpload;

/**
 * 文件Mapper接口
 * 
 * @author zyq
 * @date 2021-08-16
 */
public interface CmsUploadMapper {
    /**
     * 查询文件
     * 
     * @param uploadId 文件ID
     * @return 文件
     */
    public CmsUpload selectCmsUploadById(Long uploadId);

    /**
     * 查询文件列表
     * 
     * @param cmsUpload 文件
     * @return 文件集合
     */
    public List<CmsUpload> selectCmsUploadList(CmsUpload cmsUpload);

    /**
     * 新增文件
     * 
     * @param cmsUpload 文件
     * @return 结果
     */
    public int insertCmsUpload(CmsUpload cmsUpload);

    /**
     * 修改文件
     * 
     * @param cmsUpload 文件
     * @return 结果
     */
    public int updateCmsUpload(CmsUpload cmsUpload);

    /**
     * 删除文件
     * 
     * @param uploadId 文件ID
     * @return 结果
     */
    public int deleteCmsUploadById(Long uploadId);

    /**
     * 批量删除文件
     * 
     * @param uploadIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCmsUploadByIds(Long[] uploadIds);
}
