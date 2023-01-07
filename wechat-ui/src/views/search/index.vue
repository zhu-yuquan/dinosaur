<template>
    <div>
        <van-empty :description="msg"/>
    </div>
</template>

<script>
    import {pernovelList,getNovelDetail} from "@/api/pernovel/pernovel";

    export default {
        data() {
            return {
                msg: "搜索",
                novelList: []

            };
        },
        created() {
            this.getNovelList(1)
        },
        methods: {
            getNovelList(){
                pernovelList({pageSize: 10,pageNum:1}).then(res => {
                    console.log("res：" + res)
                    if (res.code == 200){

                    } else {
                        return Promise.reject(new Error(res.msg))
                    }
                    this.loading = false;
                })
            }


        }
    }
</script>