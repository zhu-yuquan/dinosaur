import request from '@/utils/request'


// 获取 小说详情
export function getNovelDetail(id) {
    return request({
        url: '/wechat/pernovel/view?id=' + id,
        method: 'get'
    })
}

// 小说列表
export function pernovelList(data) {
    return request({
        url: '/wechat/pernovel/list',
        method: 'get',
        params: data
    })
}

