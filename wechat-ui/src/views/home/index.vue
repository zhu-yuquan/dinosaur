<template>
    <div>
        <van-empty :description="msg"/>
    </div>
</template>

<script>
    import {login} from "@/api/wechat";
    import { setToken } from "@/utils/auth";

    export default {
        data() {
            return {
                msg: "首页11",
                openId: "", //微信openId
            };
        },
        created() {
            const openId = this.$route.query.openId;
            console.log("openId=" + openId);
            if (openId){
                this.openId = openId;
                this.getToken(openId);
            }

        },
        methods: {
            getToken(openId){
                this.loading = true;
                login({openId: openId}).then(res => {
                    console.log("登录信息：" + res)
                    if (res.code == 200){
                        const token = res.token;
                        setToken(token);
                    } else {
                        return Promise.reject(new Error(res.msg))
                    }
                    this.loading = false;
                });
            }
        }
    }
</script>