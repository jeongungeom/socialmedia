// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import IgLogin from '../components/IgLogin.vue'
import Feed from '../components/Feed.vue'
import SignUp from '../components/SignUp.vue'
import Update from '../components/Update.vue'
import Profile from '../components/Profile.vue'


const routes = [
    { path: '/', component: IgLogin },
    { path: '/feed', component: Feed, meta: {requiresAuth: true} },
    { path: '/signUp', component: SignUp, meta: {requiresAuth: true} },
    { path: '/update', component: Update, meta: {requiresAuth: true} },
    { path: '/profile', component: Profile, meta: {requiresAuth: true} },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    const isLoggedIn = !!localStorage.getItem('jwt')
    if (to.matched.some(record => record.meta && record.meta.requiresAuth)) {
        if (!isLoggedIn) {
            alert('로그인 부탁드립니다.')
            next('/')
        } else {
            next()
        }
    } else {
        next()
    }
})


export default router
