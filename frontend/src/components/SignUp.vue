<template>
  <div class="register-bg d-flex align-items-center justify-content-center min-vh-100">
    <div class="register-card p-5 rounded-4">
      <button @click="goBack" type="button" class="btn btn-outline-light btn-sm d-flex align-items-center justify-content-center me-2" style="width: 36px; height: 36px;">
        <i class="bi bi-arrow-left fs-4"></i>
      </button>
      <h2 class="mb-4 text-center text-white">회원가입</h2>
      <form @submit.prevent="onRegister">
        <div class="mb-3">
          <label class="form-label text-white">아이디</label>
          <input v-model="form.username" @blur="validateUsername" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label text-white">비밀번호</label>
          <input v-model="form.password" type="password" class="form-control" required />
        </div>
        <div class="mb-3">
          <label class="form-label text-white">이메일</label>
          <input v-model="form.email" @blur="validateEmail" class="form-control" required />
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
import {reactive, ref} from 'vue'
import api from '../api/axios.js'
import {useRouter} from "vue-router";

const router = useRouter();
const form = reactive({
  username: '',
  password: '',
  email: ''
})
const msg = ref('')
const success = ref(false)
const csrfToken = ref('')

function goBack() {
  router.back();
}

function validateUsername() {
  const usernameValid = /^[a-zA-Z0-9_]+$/.test(form.username);

  if (!usernameValid) {
    msg.value = '아이디는 영어, 숫자, 언더바(_)만 입력 가능합니다.';
    return false;
  }
  msg.value = '';
  return true;
}



function validateEmail() {
  const emailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email);

  if (!emailValid) {
    msg.value = '이메일 형식에 맞게 입력해주세요.';
    return false;
  }

  msg.value = '';
  return true;
}


const onRegister = async () => {
  msg.value = ''
  if(!validateUsername()) {
    return false;
  } else if(!validateEmail()) {
    return false;
  }

  try {
    await api.post('/auth/register', form)

    const loginRes = await api.post('/auth/login', {
      username: form.username,
      password: form.password
    });

    const token = loginRes.data.token; // 서버 응답 구조에 따라 조정
    localStorage.setItem('jwt', token);

    await router.push('/feed');

    success.value = true
    alert('회원가입 완료되었습니다.')
  } catch (e) {
    msg.value = e.response?.data || '회원가입 실패!'
    success.value = false
  }
}

api.interceptors.request.use(config => {
  const token = localStorage.getItem('jwt');
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token;
  }
  return config;
});
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
