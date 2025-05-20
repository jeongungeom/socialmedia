<template>
  <div class="insta-post-modal-bg" @click.self="close">
    <div class="insta-post-modal">
      <!-- 좌측: 게시글 이미지 -->
      <div class="insta-post-image">
        <img :src="post.imageUrl" alt="게시물 이미지" />
      </div>
      <!-- 우측: 정보/댓글 -->
      <div class="insta-post-info">
        <!-- 상단: 프로필/닉네임/옵션 -->
        <div class="insta-post-header">
          <img :src="post.profileImage" class="profile-img" alt="프로필" />
          <span class="nickname">{{ post.nickname }}</span>
          <button class="option-btn" title="더보기">
            <i class="bi bi-three-dots"></i>
          </button>
        </div>
        <div class="insta-post-content">
          <p class="caption">
            <span class="nickname">{{ post.nickname }}</span>
            {{ post.caption }}
          </p>
          <small class="created-at">{{ post.createdAt }}</small>
        </div>
        <!-- 하단: 좋아요/댓글/저장 아이콘 -->
        <div class="insta-post-actions">
          <button><i class="bi bi-heart"></i></button>
          <button><i class="bi bi-chat"></i></button>
          <button><i class="bi bi-bookmark"></i></button>
        </div>
        <!-- 댓글 입력 -->
        <form class="insta-post-comment-form" @submit.prevent="submitComment">
          <input
              v-model="comment"
              type="text"
              placeholder="댓글 달기..."
              autocomplete="off"
          />
          <button type="submit" :disabled="!comment">게시</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const props = defineProps({
  post: Object, // { imageUrl, profileImage, nickname, caption, createdAt }
  close: Function
})
const comment = ref('')
function submitComment() {
  // 댓글 등록 로직
  comment.value = ''
}
</script>

<style scoped>
.insta-post-modal-bg {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.8);
  z-index: 3000;
  display: flex;
  align-items: center;
  justify-content: center;
}
.insta-post-modal {
  display: flex;
  background: #181818;
  border-radius: 14px;
  box-shadow: 0 4px 32px rgba(0,0,0,0.25);
  max-width: 900px;
  width: 90vw;
  height: 70vh;
  min-height: 420px;
  overflow: hidden;
}
.insta-post-image {
  flex: 1.1;
  background: #000;
  display: flex;
  align-items: center;
  justify-content: center;
}
.insta-post-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background: #000;
  max-height: 70vh;
}
.insta-post-info {
  flex: 0.9;
  display: flex;
  flex-direction: column;
  background: #181818;
  color: #fff;
  padding: 24px 18px 18px 18px;
  min-width: 340px;
  max-width: 400px;
}
.insta-post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}
.profile-img {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  object-fit: cover;
  border: 1.5px solid #e1306c;
}
.nickname {
  font-weight: bold;
  font-size: 1.07rem;
}
.option-btn {
  margin-left: auto;
  background: none;
  border: none;
  color: #ddd;
  font-size: 1.3rem;
  cursor: pointer;
}
.insta-post-content {
  flex: 1;
  margin-bottom: 12px;
  overflow-y: auto;
}
.caption {
  margin: 0 0 10px 0;
  font-size: 1.08rem;
  word-break: break-all;
}
.created-at {
  color: #bbb;
  font-size: 0.97rem;
}
.insta-post-actions {
  display: flex;
  gap: 18px;
  margin-bottom: 12px;
}
.insta-post-actions button {
  background: none;
  border: none;
  color: #fff;
  font-size: 1.4rem;
  cursor: pointer;
  transition: color 0.15s;
}
.insta-post-actions button:hover {
  color: #e1306c;
}
.insta-post-comment-form {
  display: flex;
  align-items: center;
  gap: 8px;
  border-top: 1px solid #232323;
  padding-top: 10px;
}
.insta-post-comment-form input {
  flex: 1;
  background: none;
  border: none;
  color: #fff;
  font-size: 1.04rem;
  padding: 8px 4px;
  outline: none;
}
.insta-post-comment-form button {
  background: none;
  border: none;
  color: #e1306c;
  font-weight: bold;
  font-size: 1.07rem;
  cursor: pointer;
  opacity: 0.9;
  transition: opacity 0.12s;
}
.insta-post-comment-form button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
@media (max-width: 700px) {
  .insta-post-modal { flex-direction: column; height: auto; max-width: 98vw; }
  .insta-post-image, .insta-post-info { min-width: 0; max-width: none; width: 100%; }
}
</style>
