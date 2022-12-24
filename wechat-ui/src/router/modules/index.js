/**
 * 在这将每个 tabbar 作为一个路由模块
 */

// 加载当前路径下所有的文件
const routerFiles = require.context('.', true, /\.js$/)

let configRouters = []
routerFiles.keys().forEach(key => {
    //    如果是当前文件，则跳过
    if (key === './index.js') {
        return
    }
    // 读取出文件中的default模块
    configRouters = configRouters.concat(routerFiles(key).default)
})
export default configRouters // 抛出一个Vue-router期待的结构的数组
