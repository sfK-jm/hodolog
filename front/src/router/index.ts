import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import WriteView from "../views/WriteView.vue";
import ReadView from "@/views/ReadView.vue";
import EditView from "@/views/EditView.vue";
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/write",
      name: "write",
      component: WriteView,
    },
    {
      path: "/edit/:postId",
      name: "edit",
      component: EditView,
      props: true
    },
    {
      path: "/login",
      name: "DoLogin",
      component: LoginView,
      props: true
    },
    {
      path: "/post/:postId",
      name: "post",
      component: ReadView,
      props: true,
    }


    // {
    //   path: "/about",
    //   name: "about",
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import("../views/AboutView.vue"),
    // },
  ],
});

export default router;
