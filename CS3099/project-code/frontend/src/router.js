import AuthService from "@/services/AuthenticationService.js";
import { createRouter, createWebHistory } from "vue-router";

import Home from "@/views/Home.vue";
import NotFound404 from "@/views/404NotFound.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      requiresAuth: false,
      roles: ["SOLVER", "SETTER", "ADMIN"],
    },
  },
  {
    path: "/play/:puzzleID?",
    props: true,
    name: "Play",
    component: () => import("@/views/Play.vue"),
    meta: {
      requiresAuth: true,
      roles: ["SOLVER", "SETTER", "ADMIN"], // ADMIN cannot play
    },
  },
  {
    path: "/create",
    name: "Create",
    component: () => import("@/views/Create.vue"),
    meta: {
      requiresAuth: true,
      roles: ["SETTER", "ADMIN"],
    },
  },
  {
    path: "/auth/authorize",
    name: "Authorize",
    component: () => import("@/views/Authorize.vue"),
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: "/logout",
    name: "Logout",
    redirect: "/",
    meta: {
      requiresAuth: true,
      roles: ["SOLVER", "SETTER", "ADMIN"],
    },
  },
  // ADMIN ONLY
  {
    path: "/manage/users",
    name: "UserManage",
    component: () => import("@/views/admin/UserManage.vue"),
    meta: {
      requiresAuth: true,
      roles: ["ADMIN"],
    },
  },
  {
    path: "/manage/puzzles",
    name: "PuzzleManage",
    component: () => import("@/views/admin/PuzzleManage.vue"),
    meta: {
      requiresAuth: true,
      roles: ["ADMIN"],
    },
  },
  // MUST BE LAST ROUTE
  // ERROR 404 INVALID URL
  {
    path: "/:catchAll(.*)",
    component: NotFound404,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

//

//https://stackoverflow.com/questions/52653337/vuejs-redirect-from-login-register-to-home-if-already-loggedin-redirect-from
router.beforeEach((to, from, next) => {
  // Check if the user wants to logout
  if (to.redirectedFrom && to.redirectedFrom.name == "Logout") {
    AuthService.logout();
    next();
    return;
  }
  // Check if the route requires a login
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    // Get the user information from cookies
    var user = $cookies.get("user");

    // Check if the user is logged in
    if (!user) {
      next({ path: "/auth/authorize" });
      return;
    }

    // Check if the user has the required role
    if (!to.meta.roles.includes(user.role)) {
      next({ path: "/" });
      return;
    }
  }

  next();
});

export default router;
