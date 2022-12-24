import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import BusinessRouterConfig from './modules' // 引入业务逻辑模块

/* Layout */
import Layout from '@/components/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
var publicRoutes = [
    {
        path: "/",
        name: "Index",
        redirect: "/home",
        component: Layout,
        children: [
            {
                path: "home",
                name: "home",
                meta: {
                    title: "主页",
                },
                component: () => import("@/views/home/index.vue"),
            },
            {
                path: "search",
                name: "search",
                meta: {
                    title: "搜索",
                    isOpen: true,
                },
                component: () => import("@/views/search/index"),
            },
            {
                path: "person",
                name: "person",
                meta: {
                    title: "个人中心",
                },
                component: () => import("@/views/person/index.vue"),
            },
        ],
    },
    {
        path: '/login',
        component: (resolve) => require(['@/views/login/login'], resolve),
        hidden: true
    },
    {
        path: '/404',
        component: (resolve) => require(['@/views/error/404'], resolve),
        hidden: true
    },
    {
        path: '/401',
        component: (resolve) => require(['@/views/error/401'], resolve),
        hidden: true
    }
]

// 将业务路由与工共路由合并
export const constantRoutes = publicRoutes.concat(BusinessRouterConfig)



export default new Router({
    base: "/wechat-mp-vue",
    mode: 'history', // 去掉url中的#
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})
