<template>
  <div>
    <van-grid :border="false" :column-num="3">
      <van-grid-item>
        <div @click="handleHeadimgurlClick">
          <van-image width="80" height="80" round :src="userinfo.headimgurl" />
        </div>
      </van-grid-item>
      <van-grid-item>
        <van-cell :value="userinfo.nickname" :border="false" />
        <van-cell :value="userinfo.phone" :border="false" />
      </van-grid-item>
      <van-grid-item>
        <van-icon name="setting-o" size="20" />
      </van-grid-item>
    </van-grid>
    <van-empty :description="msg" />
  </div>
</template>

<script>
import { login } from "@/api/wechat";


export default {
  data() {
    return {
      msg: "个人页面",
      loading: true,
      userinfo: {
        "subscribe": 1,
        "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M",
        "nickname": "昵称",
        "sex": 1,
        "language": "zh_CN",
        "city": "广州",
        "province": "广东",
        "country": "中国",
        "headimgurl": "",
        "subscribe_time": 1382694957,
        "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL",
        "remark": "",
        "groupid": 0,
        "tagid_list": [128, 2],
        "subscribe_scene": "ADD_SCENE_QR_CODE",
        "qr_scene": 98765,
        "qr_scene_str": ""
      },
      code: "",
      unlogin: true,
    };
  },
  mounted() {
    //获取链接中的code，没请求授权之前，没有 code
    var code = this.getUrlCode().code || ''
    //授权之后,当前页面一直有 code ,判断 code 是否有效。有效则自动登录
    var codestate = localStorage.getItem("CodeState");
    if (this.unlogin && codestate == "new" && code.length > 0) {
      //判断code
      this.login(code)
    }
  },
  methods: {
    /**
     * 点击头像触发，非静默授权，第一次有弹框
     */
    handleHeadimgurlClick() {
      //获取当前页面url，作为回调页面
      var local = window.location.href
      var appid = process.env.VUE_APP_APPID
      //为登录检测是否需要调用 code
      if (this.unlogin) {
        // 截取code： 如果是回调之后的页面，链接中带 code
        var code = this.getUrlCode().code
        // 没有登陆，且 code 不存在，初次加载。请求网页授权
        if (code == null || code == '') {
          localStorage.setItem("CodeState", "new")
          //异步请求会被块域拦截。直接加载就OK
          window.location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${encodeURIComponent(local)}&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect`
        }
      } else {
        //预览头像
      }
    },
    /**
     * 截取url中的code方法
     */
    getUrlCode() {
      var url = location.search
      this.winUrl = url
      var theRequest = new Object()
      if (url.indexOf("?") != -1) {
        var str = url.substr(1)
        var strs = str.split("&")
        for (var i = 0; i < strs.length; i++) {
          theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1])
        }
      }
      return theRequest
    },
    login(code) {
      login(code).then(response => {
        console.log(response)
        this.userinfo = response.data
        //code失效，置空
        this.code = null;
        //删除存储的code
        localStorage.setItem("CodeState", "old")
        this.unlogin = false
      })

    }
  }
}
</script>