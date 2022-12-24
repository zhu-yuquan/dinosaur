package com.yuquancoco.novel.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuquancoco.common.annotation.Excel;
import com.yuquancoco.common.core.domain.entity.SysUser;
import lombok.Data;

import java.util.Date;

/**
 * 微信用户对象 wechat_user
 *
 * @author cj
 * @date 2021-06-10
 */
@Data
public class WechatUser extends SysUser {
    private static final long serialVersionUID = 1L;

    private Long wechatId;

    /**
     * 用户的标识，对当前公众号唯一
     */
    private String openid;

    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    @Excel(name = "用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。")
    private String subscribe;

    /**
     * 用户的昵称
     */
    @Excel(name = "用户的昵称")
    private String nickname;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    @Excel(name = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private String sex;

    /**
     * 用户所在城市
     */
    @Excel(name = "	用户所在城市")
    private String city;

    /**
     * 用户所在国家
     */
    @Excel(name = "用户所在国家")
    private String country;

    /**
     * 用户所在省份
     */
    @Excel(name = "用户所在省份")
    private String province;

    /**
     * 用户的语言，简体中文为zh_CN
     */
    @Excel(name = "用户的语言，简体中文为zh_CN")
    private String language;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @Excel(name = "用户头像，最后一个数值代表正方形头像大小", readConverterExp = "有=0、46、64、96、132数值可选，0代表640*640正方形头像")
    private String headImgUrl;

    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date subscribeTime;

    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    @Excel(name = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。")
    private String unionId;

    /**
     * 用户所在的分组ID（兼容旧的用户分组接口）
     */
    @Excel(name = "用户所在的分组ID", readConverterExp = "兼=容旧的用户分组接口")
    private String groupid;

    /**
     * 用户被打上的标签ID列表
     */
    @Excel(name = "用户被打上的标签ID列表")
    private String tagidList;

    /**
     * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他
     */
    @Excel(name = "返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他")
    private String subscribeScene;

    /**
     * 二维码扫码场景（开发者自定义）
     */
    @Excel(name = "二维码扫码场景", readConverterExp = "开=发者自定义")
    private String qrScene;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    @Excel(name = "二维码扫码场景描述", readConverterExp = "开=发者自定义")
    private String qrSceneStr;


}