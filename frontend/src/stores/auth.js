import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        id: null,           // 내 고유 id
        username: '',       // 내 username (원하면 추가)
        profilePicture: '', // 내 프로필 사진 (원하면 추가)
        // ... 기타 필요한 정보
    }),
    actions: {
        setUser(user) {
            this.id = user.id
            this.username = user.username
            this.profilePicture = user.profilePicture
        },
        clearUser() {
            this.id = null
            this.username = ''
            this.profilePicture = ''
        }
    },
    persist: true
})
