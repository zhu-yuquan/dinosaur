import request from '@/utils/request'

// 查询文件列表
export function listUpload(query) {
  return request({
    url: '/novel/cmsupload/list',
    method: 'get',
    params: query
  })
}

// 查询文件详细
export function getUpload(uploadId) {
  return request({
    url: '/novel/cmsupload/' + uploadId,
    method: 'get'
  })
}

// 新增文件
export function addUpload(data) {
  return request({
    url: '/novel/cmsupload',
    method: 'post',
    data: data
  })
}

// 修改文件
export function updateUpload(data) {
  return request({
    url: '/novel/cmsupload',
    method: 'put',
    data: data
  })
}

// 删除文件
export function delUpload(uploadId) {
  return request({
    url: '/novel/cmsupload/' + uploadId,
    method: 'delete'
  })
}

// 导出文件
export function exportUpload(query) {
  return request({
    url: '/novel/cmsupload/export',
    method: 'get',
    params: query
  })
}