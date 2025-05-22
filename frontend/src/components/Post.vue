<template>
  <div class="ig-post-create-2025">
    <h2 class="ig-title">새 게시물 만들기</h2>
    <form @submit.prevent="submitPost" class="ig-post-form">
      <!-- 이미지 미리보기/업로드 -->
      <div class="ig-upload-area" @click="triggerFileInput">
        <input
            type="file"
            ref="fileInput"
            accept="image/*"
            @change="onFileChange"
            style="display:none"
        />
        <div v-if="previewUrl" class="ig-image-preview">
          <img :src="previewUrl" alt="미리보기" />
        </div>
        <div v-else class="ig-upload-placeholder">
          <i class="bi bi-image" style="font-size:2.5rem; color:#bbb;"></i>
          <p>사진을 업로드하세요</p>
        </div>
      </div>

      <!-- 캡션 입력 -->
      <textarea
          v-model="caption"
          class="ig-caption-input"
          maxlength="300"
          placeholder="문구를 입력하세요 (최대 300자)"
      ></textarea>

      <!-- 등록 버튼 -->
      <button
          class="ig-post-btn"
          :disabled="!previewUrl || isUploading"
          type="submit"
      >
        {{ isUploading ? '업로드 중...' : '공유하기' }}
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from "../api/axios.js";
import { useRouter } from 'vue-router'

const router = useRouter();
const fileInput = ref(null)
const previewUrl = ref('')
const imageFile = ref(null)
const caption = ref('')
const isUploading = ref(false)

function triggerFileInput() {
  fileInput.value && fileInput.value.click()
}

function onFileChange(e) {
  const file = e.target.files[0]
  if (!file) return
  imageFile.value = file
  previewUrl.value = URL.createObjectURL(file)
}

async function submitPost() {
  if (!imageFile.value) return
  isUploading.value = true
  const formData = new FormData()
  formData.append('file', imageFile.value)
  formData.append('caption', caption.value)
  try {
    await api.post('/photo/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    alert('게시물이 업로드되었습니다!')
    await router.push('/profile')
    previewUrl.value = ''
    imageFile.value = null
    caption.value = ''
  } catch (e) {
    alert('업로드 실패!')
  } finally {
    isUploading.value = false
  }
}
</script>

<style scoped>
.ig-post-create-2025 {
  max-width: 600px;
  margin: 48px auto;
  background: #000000;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0,0,0,0.16);
  padding: 36px 32px 32px 32px;
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}
.ig-title {
  text-align: center;
  font-weight: 700;
  font-size: 1.6rem;
  margin-bottom: 28px;
  letter-spacing: 1px;
}

.ig-upload-area {
  background: #181818;
  border: 2px dashed #444;
  border-radius: 14px;
  min-height: 320px;
  margin-bottom: 22px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.18s;
  padding: 24px 0;
  position: relative;
}
.ig-upload-area:hover {
  border-color: #e1306c;
}
.ig-upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #bbb;
  pointer-events: none;
  user-select: none;
}
.ig-multi-preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
  transition: filter 0.15s;
}
.ig-multi-preview-item:hover img {
  filter: brightness(0.85);
}

.ig-caption-input {
  margin: 18px 0 22px 0;
  width: 100%;
  min-height: 120px;
  background: #181818;
  border: none;
  border-radius: 8px;
  color: #fff;
  font-size: 1.08rem;
  padding: 12px 14px;
  resize: vertical;
  outline: none;
  box-shadow: 0 0 0 1.5px #444 inset;
  transition: box-shadow 0.16s;
}
.ig-caption-input:focus {
  box-shadow: 0 0 0 2px #e1306c inset;
}

.ig-post-btn {
  width: 100%;
  background: linear-gradient(90deg, #e1306c 0%, #fdc468 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  font-weight: 700;
  font-size: 1.13rem;
  padding: 12px 0;
  margin-top: 10px;
  cursor: pointer;
  transition: background 0.18s;
}
.ig-post-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #444;
}
.ig-image-preview {
  width: 525px;
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-radius: 12px;
  background: #222;
  box-shadow: 0 2px 10px rgba(225,48,108,0.07);
}
.ig-image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
  display: block;
}
</style>
