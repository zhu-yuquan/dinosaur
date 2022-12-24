<template>
  <div style="app">
    <index-page />
  </div>
</template>

<script>
import IndexPage from './views/index/index.vue'

export default {
  name: 'App',
  components: {
    IndexPage
  }
}
</script>

<style>
  .app {
    text-align: center;
  }
</style>
