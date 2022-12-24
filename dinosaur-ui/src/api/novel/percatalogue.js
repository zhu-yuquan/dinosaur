import request from '@/utils/request'

// 查询小说章节列表
export function listCatalogue(query) {
  return request({
    url: '/novel/percatalogue/list',
    method: 'get',
    params: query
  })
}

// 查询小说章节详细
export function getCatalogue(catalogueId) {
  return request({
    url: '/novel/percatalogue/' + catalogueId,
    method: 'get'
  })
}

// 新增小说章节
export function addCatalogue(data) {
  return request({
    url: '/novel/percatalogue',
    method: 'post',
    data: data
  })
}

// 修改小说章节
export function updateCatalogue(data) {
  return request({
    url: '/novel/percatalogue',
    method: 'put',
    data: data
  })
}

// 删除小说章节
export function delCatalogue(catalogueId) {
  return request({
    url: '/novel/percatalogue/' + catalogueId,
    method: 'delete'
  })
}

// 导出小说章节
export function exportCatalogue(query) {
  return request({
    url: '/novel/percatalogue/export',
    method: 'get',
    params: query
  })
}