package com.yuquancoco.interceptor;

import com.alibaba.fastjson.JSON;
import com.yuquancoco.common.constant.HttpStatus;
import com.yuquancoco.common.core.domain.AjaxResult;
import com.yuquancoco.common.utils.ServletUtils;
import com.yuquancoco.common.utils.StringUtils;
import com.yuquancoco.component.WeChatTokenService;
import com.yuquancoco.novel.domain.WeChatLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author cj
 */
@Component
public class WeChatTokenInterceptor implements HandlerInterceptor {


    @Autowired
    private WeChatTokenService weChatTokenService;

    /**
     * 白名单
     */
    private final String[] whitelist = {
            "/login",
            "/login1",
            "/register",
            "/captchaImage",
            "/pernovel/view",

            "/perphoto/list",
            "/percatalogue/list",
            "/percatalogue/view",
            "/cmsupload/list",
            "/pervideo/view",
            "/pervideo/list"};


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestUrl = request.getRequestURI().split("/wechat")[1];
        if (Arrays.asList(whitelist).contains(requestUrl)){
            return true;
        }
        WeChatLoginUser weChatLoginUser = weChatTokenService.getLoginUser(request);
        if (weChatLoginUser != null){
            return true;
        }
        //认证失败
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
        //返回 false 则请求中断
        return false;
    }

}
