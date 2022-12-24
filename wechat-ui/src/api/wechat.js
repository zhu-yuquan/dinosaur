import request from '@/utils/request'


// 获取  openid
export function getOpenid(code) {
    return request({
        url: '/base/getOpenid/' + code,
        method: 'get'
    })
}

// 登录，返回用户详情
export function login(data) {
    return request({
        url: '/wechat/login',
        method: 'post',
        params: data
    })
}

