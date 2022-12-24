<template>
  <div>
    <van-tabbar route v-model="active" @change="handleChange" :safe-area-inset-bottom="true">
      <van-tabbar-item to="/home" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/search" icon="search">搜索</van-tabbar-item>
      <van-tabbar-item to="/person" icon="friends-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 0
    }
  },
  methods: {
    handleChange(val) {
      console.log(val)
    }
  }
}
</script>