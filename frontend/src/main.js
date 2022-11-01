import { createApp } from 'vue'
import App from './App.vue'
import Element from 'element-plus'
import 'element-plus/lib/theme-chalk/index.css'
import locale from 'element-plus/lib/locale/lang/pl'
import VueRouter from './router'
import axios from "./axios";
import VueAxios from 'vue-axios'
import store from './store'
import globalMixins from "./mixins"



const Application = createApp(App)
    .use(Element, {locale})
    .use(VueRouter)
    .use(VueAxios, axios)
    .use(store)
    .mixin(globalMixins)
    .mount('#app');

export default Application;

