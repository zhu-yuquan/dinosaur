<template>
  <div>
    <div>
      <router-view />
    </div>
  </div>
</template>

<script>

  import { login } from "@/api/wechat";
  import { setToken } from "@/utils/auth";

export default {
  data() {
    return {
    };
  },
  mounted() {
    // this.$router.replace('/home')
  },
  created(){
    console.log("index")
  },
  methods: {

  }
}
</script>