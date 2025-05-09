<template>
  <div>
    <h1>Spring Boot API 테스트</h1>
    <button @click="callApi">API 호출</button>
    <p>{{ message }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const message = ref('')

const callApi = async () => {
  try {
    // Spring Boot에서 만든 테스트 컨트롤러의 엔드포인트로 요청
    const response = await axios.get('http://localhost:8080/api/test')
    message.value = response.data
  } catch (error) {
    message.value = 'API 호출 실패: ' + error.message
  }
}
</script>
