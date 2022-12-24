import Vue from 'vue'

import App from './App.vue'

import vant from 'vant';
import 'vant/lib/index.css';

import store from './store'
import router from './router'

import { getDicts } from "@/api/system/system";

// 全局方法挂载
Vue.prototype.getDicts = getDicts;


Vue.use(vant)

new Vue({
    el: '#app',
    data() {
        return {
        }
    },
    router,
    store,
    render: h => h(App)
})
