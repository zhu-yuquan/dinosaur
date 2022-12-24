package com.yuquancoco.wx.mp.service.impl;

import com.yuquancoco.novel.domain.WechatUser;
import com.yuquancoco.novel.mapper.WechatUserMapper;
import com.yuquancoco.wx.mp.service.MpWechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信用户Service业务层处理
 *
 * @author cj
 * @date 2021-06-10
 */
@Slf4j
@Service
public class MpWechatUserServiceImpl implements MpWechatUserService {
    @Autowired
    private WechatUserMapper wechatUserMapper;

    /**
     * 查询微信用户
     *
     * @param openid 微信用户ID
     * @return 微信用户
     */
    @Override
    public WechatUser selectWechatUserById(String openid) {
        return wechatUserMapper.selectWechatUserById(openid);
    }

    /**
     * 查询微信用户列表
     *
     * @param wechatUser 微信用户
     * @return 微信用户
     */
    @Override
    public List<WechatUser> selectWechatUserList(WechatUser wechatUser) {
        return wechatUserMapper.selectWechatUserList(wechatUser);
    }

    /**
     * 新增微信用户
     *
     * @param wechatUser 微信用户
     * @return 结果
     */
    @Override
    public int insertWechatUser(WechatUser wechatUser) {
        log.info("微信openid:【{}】", wechatUser.getOpenid());
        //查询是否已存在，openid已存在不插入数据库
        WechatUser old = wechatUserMapper.selectWechatUserById(wechatUser.getOpenid());
        if (old != null){
            return 0;
        }
        return wechatUserMapper.insertWechatUser(wechatUser);
    }

    /**
     * 修改微信用户
     *
     * @param wechatUser 微信用户
     * @return 结果
     */
    @Override
    public int updateWechatUser(WechatUser wechatUser) {
        return wechatUserMapper.updateWechatUser(wechatUser);
    }

    /**
     * 批量删除微信用户
     *
     * @param openids 需要删除的微信用户ID
     * @return 结果
     */
    @Override
    public int deleteWechatUserByIds(String[] openids) {
        return wechatUserMapper.deleteWechatUserByIds(openids);
    }

    /**
     * 删除微信用户信息
     *
     * @param openid 微信用户ID
     * @return 结果
     */
    @Override
    public int deleteWechatUserById(String openid) {
        return wechatUserMapper.deleteWechatUserById(openid);
    }
}