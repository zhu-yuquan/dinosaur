package com.yuquancoco.wxbusiness.controller;

import com.yuquancoco.common.constant.Constants;
import com.yuquancoco.common.core.controller.BaseController;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.core.redis.RedisCache;
import com.yuquancoco.common.utils.uuid.IdUtils;
import com.yuquancoco.component.WeChatTokenService;
import com.yuquancoco.novel.domain.WeChatLoginUser;
import com.yuquancoco.novel.domain.WechatUser;
import com.yuquancoco.wxbusiness.service.IWechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * loginController
 *
 * @author zyq
 * @date 2021-08-16
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WxLoginController extends BaseController {

    @Autowired
    private IWechatUserService wechatUserService;
    @Autowired
    private WeChatTokenService weChatTokenService;
    /**
     * openId登录
     *
     * @param openId
     * @return
     */
    @PostMapping("/login")
    public AjaxResult wechatLogin(@RequestParam(required = true,name = "openId") String openId) {
        // openid唯一登录
        log.info("***********用户openid=【{}】", openId );

        WechatUser wechatUser = wechatUserService.selectWechatUserById(openId);
        if (wechatUser == null){
            return AjaxResult.error("无该用户信息");
        }
        String token = weChatTokenService.createToken(new WeChatLoginUser(wechatUser,null));
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * openId登录
     *
     * @param openId
     * @return
     */
    @GetMapping("/login1")
    public AjaxResult login(String openId) {
        // openid唯一登录
        log.info("***********用户openid=【{}】", openId );

        WechatUser wechatUser = wechatUserService.selectWechatUserById(openId);
        String token = weChatTokenService.createToken(new WeChatLoginUser(wechatUser,null));
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    /**
     * 用户登录获取用户信息
     *
     * @return
     */
    @PostMapping("/getUserInfo")
    public AjaxResult wechatLogin() {
        //当前登录用户信息
        log.info("***********用户openid=【{}】", 1);

        WechatUser wechatUser = wechatUserService.selectWechatUserById(null);
        String token = weChatTokenService.createToken(new WeChatLoginUser(wechatUser,null));
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

}
