<template>
  <div class="register-bg d-flex align-items-center justify-content-center min-vh-100">
    <div class="register-card p-5 rounded-4">
      <h2 class="mb-4 text-center text-white">회원가입</h2>
      <form @submit.prevent="onRegister">
        <input type="hidden" name="_csrf" :value="csrfToken" />
        <div class="mb-3">
          <label class="form-label text-white">아이디</label>
          <input v-model="form.username" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label text-white">비밀번호</label>
          <input v-model="form.password" type="password" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label text-white">이메일</label>
          <input v-model="form.email" class="form-control" required />
        </div>
        <button class="btn btn-primary w-100">회원가입</button>
      </form>
      <div v-if="msg" class="alert mt-3" :class="{'alert-success': success, 'alert-danger': !success}">
        {{ msg }}
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import api from '../api/axios.js'


const form = reactive({
  username: '',
  password: '',
  email: ''
})
const msg = ref('')
const success = ref(false)
const csrfToken = ref('')

onMounted(async () => {
  console.log("mounted")
  // const res = await api.get('/auth/csrf-token')
  // csrfToken.value = res.data.token // 서버 응답 구조에 따라 다름
})

const onRegister = async () => {
  msg.value = ''
  try {
    await api.post('/auth/register', form)
    msg.value = '회원가입 성공! 이제 로그인하세요.'
    success.value = true
  } catch (e) {
    msg.value = e.response?.data || '회원가입 실패!'
    success.value = false
  }
}
</script>

<style scoped>
.register-bg {
  background: linear-gradient(135deg, #1a1a1a 0%, #121212 100%);
  min-height: 100vh;
}
.register-card {
  width: 100%;
  max-width: 500px;
  background: rgba(30, 30, 30, 0.96);
  box-shadow: 0 8px 32px rgba(0,0,0,0.8);
  border: 1px solid rgba(255,255,255,0.08);
}
.form-label, .text-white {
  color: #fff !important;
}
</style>
