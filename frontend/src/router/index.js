// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import IgLogin from '../components/IgLogin.vue'
import Feed from '../components/Feed.vue'
import SignUp from '../components/SignUp.vue'
import Update from '../components/Update.vue'
import Profile from '../components/Profile.vue'


const routes = [
    { path: '/', component: IgLogin },
    { path: '/feed', component: Feed},
    { path: '/signUp', component: SignUp},
    { path: '/update', component: Update},
    { path: '/profile', component: Profile},
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
