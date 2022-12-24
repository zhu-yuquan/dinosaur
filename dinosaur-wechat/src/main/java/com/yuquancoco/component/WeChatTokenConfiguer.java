package com.yuquancoco.component;

import com.yuquancoco.interceptor.WeChatTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cj
 */
@Configuration
public class WeChatTokenConfiguer implements WebMvcConfigurer {

    @Autowired
    WeChatTokenInterceptor weChatTokenInterceptor;

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(weChatTokenInterceptor).addPathPatterns("/wechat/**");
    }

}
