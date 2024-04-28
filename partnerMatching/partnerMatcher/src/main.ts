import { createApp } from 'vue'
import "vant/lib/index.css";
import App from './App.vue'
import { Button } from 'vant';
import * as VueRouter from 'vue-router'
import { NavBar,Tabbar, TabbarItem,Form,Field,CellGroup,Toast} from 'vant';
import routes from '../src/config/route.ts'
// 3. 创建路由实例并传递 `routes` 配置
// 你可以在这里输入更多的配置，但我们在这里
// 暂时保持简单
const router = VueRouter.createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHashHistory(),
    routes, // `routes: routes` 的缩写
})

// 确保 _use_ 路由实例使
// 整个应用支持路由。
const app=createApp(App)
app.use(Button);
app.use(NavBar);
app.use(Tabbar);
app.use(TabbarItem);
app.use(router);
app.use(Toast);
app.use(CellGroup);
app.use(Field);
app.use(Form);
app.mount("#app")