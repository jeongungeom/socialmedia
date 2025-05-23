import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        id: '',
        token: ''
    }),
    actions: {
        setUser({ id, username, token }) {
            this.id = id
            this.token = token
        },
        clearUser() {
            this.id = ''
            this.token = ''
        }
    },
    persist: true
})
