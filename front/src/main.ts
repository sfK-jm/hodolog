import "reflect-metadata";

import { createApp } from "vue";

import App from "./App.vue";
import router from "./router";

import 'normalize.css';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import "bootstrap/dist/css/bootstrap-utilities.css"

const app = createApp(App);

app.use(router);
app.use(ElementPlus);

app.mount("#app");
