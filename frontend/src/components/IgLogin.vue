<template>
  <div class="ig-login-bg d-flex align-items-center justify-content-center min-vh-100">
    <div class="ig-login-card p-5 rounded-4">
      <!-- 로고 -->
      <div class="text-center mb-4">
        <i class="bi bi-instagram" style="font-size:3.5rem; color:#e1306c;"></i>
        <h1 class="mt-2 ig-logo-text">Eomstagram</h1>
      </div>

      <!-- 로그인 폼 -->
      <form @submit.prevent="handleLogin" class="mb-4">
        <div class="form-floating mb-3">
          <input
              v-model="form.username"
              type="text"
              class="form-control"
              placeholder="사용자 이름"
              required
          >
          <label>사용자 이름</label>
        </div>

        <div class="form-floating mb-3">
          <input
              v-model="form.password"
              type="password"
              class="form-control"
              placeholder="비밀번호"
              required
          >
          <label>비밀번호</label>
        </div>

        <button class="btn btn-primary w-100 py-2 fw-bold">로그인</button>
        <div v-if="errorMsg" class="error">{{ errorMsg }}</div>
      </form>

      <!-- 소셜 로그인 -->
      <div class="text-center mb-4">
          <span class="text-decoration-none">또는</span>
      </div>

      <!-- 회원가입 링크 -->
      <div class="text-center">
        <small class="text-decoration-none">
          계정이 없으신가요?
          <a href="#" class="text-decoration-none" @click.prevent="register">가입하기</a>
        </small>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue'
import { useRouter } from 'vue-router'
import api from "../api/axios.js";
import {useUserStore} from "../stores/stores.js";

const userStore = useUserStore();
const errorMsg = ref(""); // Vue 예시
const router = useRouter();
const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  errorMsg.value = "";
  try {
    const loginRes = await api.post('/auth/login', {
      username: form.username,
      password: form.password
    });
    const token = loginRes.data.token;
    userStore.setUser({token: loginRes.data.token })
    parseJwt(token);
    localStorage.setItem('jwt', token);
    const payload = parseJwt(token)

    userStore.setUser({
      id: payload.id,
      username: payload.sub,
      token
    })
    console.log(payload.id)
    await router.push('/feed');
  } catch (e) {
    errorMsg.value = e.response?.data || "로그인에 실패했습니다.";
  }
};

function parseJwt(token) {
  const base64Url = token.split('.')[1]
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
  const jsonPayload = decodeURIComponent(
      atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
  )
  return JSON.parse(jsonPayload)
}

function register() {
  router.push('/signUp')
}
</script>

<style scoped>
.ig-login-bg {
  background: linear-gradient(135deg, #1a1a1a 0%, #121212 100%);
  color: #eee;
}
.ig-login-card {
  width: 100%;
  max-width: 540px;      /* 기존 450px → 540px로 확대 */
  padding: 3rem 2.5rem;  /* 상하 3rem, 좌우 2.5rem로 여백도 넉넉하게 */
  background: rgba(30, 30, 30, 0.95);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.ig-logo-text {
  color: #fafafa;
}

.error {
  color: #e74c3c;
  margin-top: 10px;
  font-size: 1rem;
  font-weight: bold;
  text-align: center;
}
</style>
