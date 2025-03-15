import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import layoutView from '@/layout/layoutView.vue';

const routes = [
  {
    path: '/',
    component: layoutView,
    meta: { requiresAuth: true }, // 父路由需要登录
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'home',
        component: HomeView,
        meta: { requiresAuth: true } // 子路由需要登录
      },
      {
        path: 'todo',
        name: 'todo',
        component: () => import('../views/TodoView.vue'),
        meta: { requiresAuth: true } // 子路由需要登录
      },
      {
        path: 'person',
        name: 'person',
        component: () => import('../views/Person.vue'),
        meta: { requiresAuth: true } // 子路由需要登录
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue') // 不需要登录
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register.vue') // 不需要登录
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  const isLoggedIn = !!sessionStorage.getItem('user'); // 检查用户是否登录

  if (to.meta && to.meta.requiresAuth && !isLoggedIn) {
    // 如果需要登录且用户未登录，重定向到登录页面
    next('/login');
  } else {
    // 否则放行
    next();
  }
});

export default router;