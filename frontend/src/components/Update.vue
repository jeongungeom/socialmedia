<template>
  <div class="edit-profile-bg">
    <div class="card shadow edit-profile-card">
      <div class="card-header d-flex align-items-center edit-profile-header">
        <i class="bi bi-person-circle me-2 fs-3"></i>
        <h4 class="mb-0">프로필 수정</h4>
      </div>
      <div class="card-body">
        <form @submit.prevent="updateProfile">
          <div class="mb-3">
            <label class="form-label">이름</label>
            <input v-model="form.username" type="text" class="form-control edit-input" required />
          </div>
          <div class="mb-3">
            <label class="form-label">소개</label>
            <input v-model="form.bio" type="text" class="form-control edit-input" required />
          </div>
          <div class="mb-3">
            <label class="form-label">이메일</label>
            <input v-model="form.email" type="email" class="form-control edit-input" required />
          </div>
          <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input v-model="form.password" type="password" class="form-control edit-input" required autocomplete="new-password" />
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <button type="button" class="btn btn-link p-0 edit-back-btn" @click="goBack" title="뒤로가기">
              <i class="bi bi-arrow-left fs-3"></i>
            </button>
            <button type="submit" class="btn btn-primary edit-save-btn">저장</button>
          </div>
          <div class="d-flex justify-content-end mt-4">
            <button type="button" class="btn btn-outline-danger edit-delete-btn" @click="deleteAccount">
              <i class="bi bi-trash"></i> 계정 삭제
            </button>
          </div>
        </form>
        <div v-if="msg" :class="{'text-success': success, 'text-danger': !success}" class="mt-3 text-center">
          {{ msg }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from "../api/axios.js";
import {useRouter} from "vue-router";

const router = useRouter();
const form = ref({ username: '', bio: '', email: '', password: ''})
const msg = ref('')
const success = ref(false)


async function updateProfile() {
  try {
    await api.put('/auth/update', form.value, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    })
    alert("수정완료");
    success.value = true
    await router.push("/profile")
  } catch (e) {
    msg.value = e.response?.data || '수정 실패'
    success.value = false
  }
}

onMounted(async () => {
  try {
    const res = await api.get('/auth/profile', {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    })
    form.value = res.data
  } catch (e) {
    msg.value = '프로필 정보를 불러올 수 없습니다.'
    success.value = false
  }
})

function goBack() {
  router.back();
}

function deleteAccount() {

  if(form.value.password === null) {
    alert('비밀번호를 입력해주세요');
    return false;
  }

  if (confirm('정말 삭제하시겠습니까?')) {
    try {
      api.put('/auth/deleteUser', form.value, {
        headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}
      })
      alert("삭제되었습니다.");
      success.value = true
      router.push("/")
    } catch (e) {
      msg.value = e.response?.data || '수정 실패'
      success.value = false
    }
  }
}
</script>

<style scoped>
.edit-profile-bg {
  min-height: 100vh;
  background: #111;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 60px;
}
.edit-profile-card {
  background: #181818;
  color: #fafafa;
  border: none;
  max-width: 500px;
  width: 100%;
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(0,0,0,0.16);
}
.edit-profile-header {
  background: #232323;
  color: #fafafa;
  border-bottom: 1px solid #222;
}
.form-label {
  color: #bbb;
}
.edit-input {
  background: #222;
  color: #fafafa;
  border: 1px solid #333;
}
.edit-input:focus {
  background: #232323;
  color: #fff;
  border-color: #666;
  box-shadow: none;
}
.edit-back-btn {
  color: #bbb !important;
}
.edit-save-btn {
  background: #e1306c;
  border: none;
  color: #fff;
  font-weight: 600;
  padding: 7px 28px;
  border-radius: 8px;
}
.edit-save-btn:hover {
  background: #c41e5a;
}
.edit-delete-btn {
  border-color: #e1306c !important;
  color: #e1306c !important;
  font-weight: 500;
  background: transparent;
}
.edit-delete-btn:hover {
  background: #2a000c;
  color: #fff !important;
  border-color: #e1306c !important;
}
.text-success {
  color: #4fe57a !important;
}
.text-danger {
  color: #ff5a5a !important;
}
</style>
