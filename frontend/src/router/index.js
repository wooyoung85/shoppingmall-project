import Vue from "vue";
import VueRouter from "vue-router";
import MainPage from "../views/MainPage.vue";
import RegisterUser from '../views/RegisterUser.vue'
import LoginUser from '../views/LoginUser.vue'
import ProductDetail from '../views/ProductDetail.vue'
import TestPage from '../views/TestPage.vue'

Vue.use(VueRouter);

const ProductList = () => { return import(/* webpackChunkName: "product" */ "../views/ProductList.vue")}

const routes = [
  {
    path: "/",
    name: "main",
    component: MainPage
  },
  {
    path: "/register",
    name: "register",
    component: RegisterUser
  },
  {
    path: "/login",
    name: "login",
    component: LoginUser
  },
  {
    path: "/product",
    name: "product",
    // route level code-splitting
    // this generates a separate chunk (product.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: ProductList
  },
  {
    path: "/product/:productId",
    name: "productDetail",
    component: ProductDetail
  },
  {
    path: "/test",
    name: "test",
    component: TestPage
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
