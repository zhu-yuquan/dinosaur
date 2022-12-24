import request from '@/utils/request'

// 查询小说列表
export function listNovel(query) {
  return request({
    url: '/novel/pernovel/list',
    method: 'get',
    params: query
  })
}

// 查询小说列表
export function getAllListNovel(query) {
  return request({
    url: '/novel/pernovel/all_list',
    method: 'get',
    params: query
  })
}

// 更新小说章节
export function updateCatalogueList(data) {
  return request({
    url: '/novel/pernovel/update_catalogue',
    method: 'get',
    params: data
  })
}

// 获取爬虫 /noveldownload  /reptile-download  /reptile-dkcwl-download
export function getReptile(data) {
  return request({
    url: '/reptile-download',
    method: 'get',
    params: data
  })
}

// 查询小说详细
export function getNovel(novelId) {
  return request({
    url: '/novel/pernovel/' + novelId,
    method: 'get'
  })
}

// 新增小说
export function addNovel(data) {
  return request({
    url: '/novel/pernovel',
    method: 'post',
    data: data
  })
}

// 修改小说
export function updateNovel(data) {
  return request({
    url: '/novel/pernovel',
    method: 'put',
    data: data
  })
}

// 删除小说
export function delNovel(novelId) {
  return request({
    url: '/novel/pernovel/' + novelId,
    method: 'delete'
  })
}

// 导出小说
export function exportNovel(query) {
  return request({
    url: '/novel/pernovel/export',
    method: 'get',
    params: query
  })
}
