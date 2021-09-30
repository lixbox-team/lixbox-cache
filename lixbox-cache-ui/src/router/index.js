import Vue from "vue";
import Router from "vue-router";
import Cache from "@/view/cache.vue";

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: "/",
      redirect: { name: "cache" },
    },
    {
      path: "/cache",
      name: "cache",
      component: Cache,
    },
  ],
});
