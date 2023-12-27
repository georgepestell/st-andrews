import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/guests',
      name: 'guests',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/GuestsView.vue'),
      meta: {
        requiresInvitation: true
      }
    },
    {
      path: '/guests/login',
      name: 'guestsLogin',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/GuestsLogin.vue')
    },
    {
      path: '/organisers/login',
      name: 'organisersLogin',
      component: () => import('../views/OrganisersLogin.vue')
    },
    {
      path: '/organisers',
      name: 'organisers',
      component: () => import('../views/OrganisersView.vue'),
      meta: {
        requiresOrganiser: true
      }
    },
    {
      path: '/organisers/logout',
      redirect: () => {
        const organiserStore = useOrganiserStore()
        organiserStore.logout()
        return { path: '/' }
      }
    }
  ]
})
import { useOrganiserStore } from '../stores/organiser'
import { useGuestStore } from '../stores/guest'

router.beforeEach(async (to, from, next) => {
  // Check if the user needs to be an organiser
  if (to.matched.some((record) => record.meta.requiresOrganiser)) {
    // Check if the organiser is logged in
    const organiserStore = useOrganiserStore()
    await organiserStore.fetch()
    if (!organiserStore.isAuth) {
      next({ path: '/organisers/login' })
      return
    }
  }

  // Check if the user needs an invitation code
  if (to.matched.some((record) => record.meta.requiresInvitation)) {
    const guestStore = useGuestStore()
    if (!guestStore.invitationCode) {
      next({ path: '/guests/login' })
      return
    }
  }

  next()
})

export default router
