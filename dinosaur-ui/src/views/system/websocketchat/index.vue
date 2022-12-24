<template>
  <div class="app-container">

    <input v-model="message" type="text" />
    <button @click="websocketsend()">Send</button>
    <div id="content"></div>

  </div>
</template>

<script>
  import { createSocket, sendWSPush, oncloseWS} from "@/api/system/websocketchat";
  export default {
    name: "WebSocketChat",
    components: {
    },
    data() {
      return {
        websock: null,
        message: null,// 发送的消息
      }
    },
    created() {
      this.initWebSocket();
      // 注册监听事件
      window.addEventListener('onmessageWS', this.getsocketData);
    },
    destroyed() {
      // this.websock.close() //离开路由之后断开websocket连接
      // 在需要的时候卸载监听事件，比如离开页面
      window.removeEventListener('onmessageWS', getsocketData);
    },
    methods: {
      initWebSocket(){ //初始化weosocket
        // /test/one 自己给自己发
        // /test/oneToMany 群发
        // /test/oneToOne 自己发送给另一个人
        const wsuri = "ws://192.168.0.50:8081/test/oneToOne";
        createSocket(wsuri);
      },
      websocketonopen(){ //连接建立之后执行send方法发送数据
        let actions = {"message":"12345"};
        this.websocketsend(JSON.stringify(actions));
      },
      websocketonerror(){//连接建立失败重连
        this.initWebSocket();
      },
      // 接收消息
      getsocketData(e){  // 创建接收消息函数
        const data = e && e.detail.data
        console.log("接收的消息：" + data)
      },
      websocketsend(){//数据发送
        let actions = {"message":this.message};
        console.log("发送的消息：" + JSON.stringify(actions))
        sendWSPush(JSON.stringify(actions));
      },
    }
  };
</script>
