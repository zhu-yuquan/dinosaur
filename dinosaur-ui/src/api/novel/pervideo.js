import request from '@/utils/request'

// 查询视频列表
export function listPervideo(query) {
  return request({
    url: '/novel/pervideo/list',
    method: 'get',
    params: query
  })
}

// 查询视频详细
export function getPervideo(videoId) {
  return request({
    url: '/novel/pervideo/' + videoId,
    method: 'get'
  })
}

// 新增视频
export function addPervideo(data) {
  return request({
    url: '/novel/pervideo',
    method: 'post',
    data: data
  })
}

// 修改视频
export function updatePervideo(data) {
  return request({
    url: '/novel/pervideo',
    method: 'put',
    data: data
  })
}

// 删除视频
export function delPervideo(videoId) {
  return request({
    url: '/novel/pervideo/' + videoId,
    method: 'delete'
  })
}
