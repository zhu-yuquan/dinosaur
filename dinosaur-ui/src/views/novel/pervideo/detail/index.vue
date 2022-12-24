<template>
  <div class="app-container">
    <vue-dplayer :videoName="pervideo.videoName" :picUrl="pervideo.videoImage" :videoUrl="pervideo.videoUrl" ></vue-dplayer>
  </div>
</template>

<script>
  import { listPervideo, getPervideo, delPervideo, addPervideo, updatePervideo } from "@/api/novel/pervideo";
  import VueDplayer from './dPlayer';
  export default {
    name: "PervideoDeatail",
    components: {
      VueDplayer
    },
    data() {
      return {
        // 遮罩层
        loading: true,

        // 视频表格数据
        pervideoList: [],
        pervideo: {}

      };
    },
    created() {
      const videoId = this.$route.params && this.$route.params.videoId;
      this.getPervideo(videoId);
    },
    methods: {
      /** 查询视频详情 */
      getPervideo(videoId) {
        this.loading = true;
        getPervideo(videoId).then(response => {
          this.pervideo = response.data;
          this.loading = false;
        });
      },

    }
  };
</script>
