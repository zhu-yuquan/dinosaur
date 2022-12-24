import request from '@/utils/request'

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
    return request({
        url: '/system/dict/data/type/' + dictType,
        method: 'get'
    })
}


// 获取验证码
export function getCodeImg() {
    return request({
        url: '/system/captchaImage',
        method: 'get'
    })
}