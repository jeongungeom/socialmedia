// src/stores/auth.js
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        token: localStorage.getItem('token') || ''
    }),
    actions: {
        setUser(user) {
            this.user = user
        },
        setToken(token) {
            this.token = token
            localStorage.setItem('token', token)
        },
        logout() {
            this.user = null
            this.token = ''
            localStorage.removeItem('token')
        }
    }
})
