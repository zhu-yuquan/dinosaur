package com.yuquancoco.wxbusiness.service;

import com.yuquancoco.novel.domain.WechatUser;

import java.util.List;

/**
 * 微信用户Service接口
 *
 * @author cj
 * @date 2021-06-10
 */
public interface IWechatUserService {
    /**
     * 查询微信用户
     *
     * @param openid 微信用户ID
     * @return 微信用户
     */
    public WechatUser selectWechatUserById(String openid);

    /**
     * 查询微信用户列表
     *
     * @param wechatUser 微信用户
     * @return 微信用户集合
     */
    public List<WechatUser> selectWechatUserList(WechatUser wechatUser);

    /**
     * 新增微信用户
     *
     * @param wechatUser 微信用户
     * @return 结果
     */
    public int insertWechatUser(WechatUser wechatUser);

    /**
     * 修改微信用户
     *
     * @param wechatUser 微信用户
     * @return 结果
     */
    public int updateWechatUser(WechatUser wechatUser);

    /**
     * 批量删除微信用户
     *
     * @param openids 需要删除的微信用户ID
     * @return 结果
     */
    public int deleteWechatUserByIds(String[] openids);

    /**
     * 删除微信用户信息
     *
     * @param openid 微信用户ID
     * @return 结果
     */
    public int deleteWechatUserById(String openid);
}