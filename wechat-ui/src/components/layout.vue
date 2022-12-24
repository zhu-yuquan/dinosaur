<template>
  <div>
    <div class="footer-view">
      <router-view></router-view>
    </div>
    <nofeel-tabbar />
  </div>
</template>

<script>
import tabbar from './tabbar.vue'
export default {
  data() {
    return {
      msg: "搜索"
    };
  },
  components: {
    'nofeel-tabbar': tabbar
  },
  methods: {
  }
}
</script>

<style scoped lang="scss">
  .footer-view {
    height: calc(100vh - 100px);
    overflow: auto;
  }
</style>