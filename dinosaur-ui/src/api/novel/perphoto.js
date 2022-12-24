import request from '@/utils/request'

// 查询相册列表
export function listPhoto(query) {
  return request({
    url: '/novel/perphoto/list',
    method: 'get',
    params: query
  })
}

// 查询相册详细
export function getPhoto(photoId) {
  return request({
    url: '/novel/perphoto/' + photoId,
    method: 'get'
  })
}

// 新增相册
export function addPhoto(data) {
  return request({
    url: '/novel/perphoto',
    method: 'post',
    data: data
  })
}

// 修改相册
export function updatePhoto(data) {
  return request({
    url: '/novel/perphoto',
    method: 'put',
    data: data
  })
}

// 删除相册
export function delPhoto(photoId) {
  return request({
    url: '/novel/perphoto/' + photoId,
    method: 'delete'
  })
}

// 导出相册
export function exportPhoto(query) {
  return request({
    url: '/novel/perphoto/export',
    method: 'get',
    params: query
  })
}