<template>
  <div class="container mt-5" style="max-width: 500px;">
    <div class="card shadow">
      <div class="card-header bg-primary text-white d-flex align-items-center">
        <i class="bi bi-person-circle me-2 fs-3"></i>
        <h4 class="mb-0">프로필 수정</h4>
      </div>
      <div class="card-body">
        <form @submit.prevent="updateProfile">
          <div class="mb-3">
            <label class="form-label">이름</label>
            <input v-model="form.username" type="text" class="form-control" required />
          </div>
          <div class="mb-3">
            <label class="form-label">소개</label>
            <input v-model="form.bio" type="text" class="form-control" required />
          </div>
          <div class="mb-3">
            <label class="form-label">이메일</label>
            <input v-model="form.email" type="email" class="form-control" required />
          </div>
          <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input v-model="form.password" type="password" class="form-control" required autocomplete="new-password" />
          </div>
          <div class="d-flex justify-content-between align-items-center">
            <button type="button" class="btn btn-link p-0" @click="goBack" title="뒤로가기">
              <i class="bi bi-arrow-left fs-3"></i>
            </button>
            <button type="submit" class="btn btn-primary">저장</button>
          </div>
          <div class="d-flex justify-content-end mt-4">
            <button type="button" class="btn btn-outline-danger" @click="deleteAccount">
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
