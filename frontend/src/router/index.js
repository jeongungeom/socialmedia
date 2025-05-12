// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import IgLogin from '../components/IgLogin.vue'
import Feed from '../components/Feed.vue'
import SignUp from '../components/SignUp.vue'


const routes = [
    { path: '/', component: IgLogin },
    { path: '/feed', component: Feed},
    { path: '/signUp', component: SignUp}
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
