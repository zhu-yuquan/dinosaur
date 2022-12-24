package com.yuquancoco.wx.mp.controller;

import com.yuquancoco.novel.domain.WechatUser;
import com.yuquancoco.wx.mp.service.MpWechatUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Edward
 */
@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/wx/redirect/{appid}")
public class WxRedirectController {
    private final WxMpService wxService;
    @Autowired
    private MpWechatUserService mpWechatUserService;

    @RequestMapping("/greet")
    public String greetUser(@PathVariable String appid, @RequestParam String code, ModelMap map) {
        if (!this.wxService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        WxOAuth2UserInfo user = null;
        try {
            WxOAuth2AccessToken accessToken = wxService.getOAuth2Service().getAccessToken(code);
            user = wxService.getOAuth2Service().getUserInfo(accessToken, null);
            log.info("微信授权用户：" + user.toString());
            WechatUser wechatUser = new WechatUser();
            BeanUtils.copyProperties(user, wechatUser);
            wechatUser.setSex(user.getSex().toString());
            log.info("微信授权用户：" + wechatUser.toString());
            mpWechatUserService.insertWechatUser(wechatUser);


            map.put("user", user);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "redirect:http://wechat.yuquancoco.cn/wechat-mp-vue/home?openId=" + user.getOpenid();
    }
}
