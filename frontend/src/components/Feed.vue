<template>
  <div class="ig-feed-container d-flex">
    <!-- 메인 피드 (4:5 비율 최신 포맷) -->
    <main class="ig-main-feed flex-grow-1 p-4">
      <div class="row justify-content-center">
        <div class="col-lg-6">
          <article
              v-for="post in posts"
              :key="post.id"
              class="card mb-4 border-0 shadow-sm"
          >
            <div class="card-header bg-white d-flex align-items-center">
              <img
                  :src="`/api/auth/image/${post.profilePicture}`"
                  class="rounded-circle me-2"
                  width="40"
                  height="40"
              >
              <strong>{{ post.username }}</strong>
            </div>
            <img
                :src="encodeURI(post.imageUrl)"
                class="card-img-top"
                alt="게시물 이미지"
                style="aspect-ratio: 4/5; object-fit: cover; cursor: pointer"
                @click="goDetail(post.id, post.userId)"
            >
            <div class="card-body">
              <div class="d-flex align-items-center gap-2 mb-3">
                <!-- 좋아요 버튼과 숫자 -->
                <button
                    @click="toggleLike(post.id)"
                    :class="['like-btn', { liked: post.isLike }]"
                    style="background: none; border: none; padding: 0; margin-right: 3px;"
                >
                  <i :class="post.isLike ? 'bi-heart-fill fs-5' : 'bi-heart fs-5'"></i>
                </button>
                <span v-if="post.likeCnt > 0" class="like-count" style="font-size:1.07rem; margin-right: 12px;">
      {{ post.likeCnt }}
    </span>
                <button
                    class="comment-btn"
                    style="background: none; border: none; padding: 0;"
                    title="댓글"
                >
                  <i class="bi bi-chat fs-5"></i>
                </button>
              </div>
              <p class="card-text">
                <strong>{{ post.username }}</strong>
                {{ post.caption }}
              </p>
            </div>
          </article>
        </div>
      </div>
    </main>
  </div>

  <div v-if="showDetail" class="insta-post-modal-bg">
    <div class="insta-post-modal">
      <!-- X 닫기 버튼 -->
      <button class="close-btn" @click="closeDetail" title="닫기">
        <i class="bi bi-x-lg"></i>
      </button>
      <!-- 좌측: 게시글 이미지 -->
      <div class="insta-post-image">
        <img :src="photoOne.imageUrl" alt="게시물 이미지" />
      </div>
      <div class="insta-post-info">
        <!-- 상단: 프로필/닉네임/옵션 -->
        <div class="insta-post-header">
          <img :src="`/api/auth/image/${photoOne.profilePicture}`" class="profile-img" alt="프로필" />
          <span class="nickname">{{ photoOne.username }}</span>
        </div>
        <div class="header-divider"></div>
        <div class="insta-post-content">
          <p class="caption">
            <span class="nickname">{{ photoOne.username }}</span>
            {{ photoOne.caption }}
          </p>
        </div>
        <!-- 하단: 좋아요/댓글/저장 아이콘 -->
        <div class="insta-post-actions">
          <!-- 좋아요 버튼과 숫자 -->
          <button
              @click="toggleLike(photoOne.id)"
              :class="{ liked: photoOne.isLike }"
              class="like-btn"
          >
            <i :class="photoOne.isLike ? 'bi-heart-fill' : 'bi-heart'"></i>
          </button>
          <span v-if="photoOne.likeCnt > 0" class="like-count">{{ photoOne.likeCnt }}</span>
          <button><i class="bi bi-chat"></i></button>
        </div>
        <small class="created-at">{{ photoOne.createdAt }}</small>
        <!-- 댓글 입력: 내 프로필 사진 추가 -->
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
import {onMounted, ref} from 'vue'
import api from "../api/axios.js";

const isMe = ref(false);
const showDetail = ref(false);
const comment = ref('');
const posts = ref([

])
const photoOne = ref({
  id: '',
  imageUrl: '',
  profilePicture: '',
  username: '',
  caption: '',
  createdAt: '',
  likeCnt: 0,
  isLike: false
})

onMounted(async () => {
  await rendering();
});

async function rendering() {
  try {
    const res = await api.get('/photo/allPhotos', {
      headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}
    })
    posts.value = res.data;
  } catch (e) {
    console.log(e.response.data);
  }
}
async function goDetail(photoId, userId) {

  try {
    const res = await api.get('/auth/photoOne', {
      params: { userId:  userId, id: photoId},
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    });
    showDetail.value = true
    console.log(res.data);
    photoOne.value = res.data;
    // if(photoOne.value.isLike === 0) {
    //   photoOne.value.isLike = false;
    // }
  } catch (e) {
    console.log(e.response.data);
  }
}

async function toggleLike(photoId) {
  const post = posts.value.find(p => p.id === photoId)

  if (post.isLike) {
    // 좋아요 취소
    post.isLike = false
    photoOne.value.isLike = false;
    await api.delete(`/like/deleteLike/${photoId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    })
    await rendering();
  } else {
    // 좋아요 추가
    post.isLike = true
    photoOne.value.isLike = true;
    await api.post('/like/addLike', { photoId: photoId }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    })
    // 필요하다면 좋아요 수 갱신
    await rendering();
  }
}

function closeDetail() {
  showDetail.value = false
}

function submitComment() {
  comment.value = ''
}

</script>

<style scoped>
.ig-feed-container {
  min-height: 100vh;
  background: #121212;
  color: #ddd;
}
.ig-main-feed {
  max-width: 700px;
  margin: 0 auto;
  color: #eee;
}
.card {
  background: #000 !important;
  border: none;
  color: #eee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.7);
}
.card-header {
  background: #000 !important;
  border-bottom: none;
  color: #eee;
}

.card-text {
  color: #ddd;
}
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
.close-btn {
  position: absolute;
  top: 16px;
  right: 18px;
  background: none;
  border: none;
  color: #fff;
  font-size: 1.6rem;
  z-index: 10;
  cursor: pointer;
  transition: color 0.15s;
}
.close-btn:hover {
  color: #e1306c;
}

.option-dropdown-wrap {
  position: relative;
  display: inline-block;
}
.option-btn {
  background: none;
  border: none;
  color: #ddd;
  font-size: 1.3rem;
  cursor: pointer;
  margin-left: 10px;
}
.option-dropdown {
  position: absolute;
  top: 30px;
  right: 0;
  background: #232323;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.18);
  min-width: 90px;
  z-index: 20;
  display: flex;
  flex-direction: column;
}
.option-dropdown button {
  background: none;
  border: none;
  color: #fff;
  font-size: 1.02rem;
  text-align: left;
  padding: 10px 18px;
  cursor: pointer;
  transition: background 0.14s, color 0.14s;
}
.option-dropdown button:hover {
  background: #181818;
  color: #e1306c;
}

.insta-post-comment-form {
  display: flex;
  align-items: center;
  gap: 8px;
  border-top: 1px solid #232323;
  padding-top: 10px;
}
.ig-main-feed {
  max-width: 500px;
  margin: 0 auto;
  color: #eee;
}
.col-lg-6 {
  max-width: 100%;
  flex: 0 0 100%;
}
.like-btn.liked i {
  color: #e1306c;
}
.like-btn i {
  color: #fff;
  transition: color 0.16s;
}
.like-count {
  color: #fff;
  font-weight: 500;
  margin-left: -2px;
  vertical-align: middle;
}
.comment-btn i {
  color: #fff;
}
button .bi.bi-chat,
button .bi.bi-chat:hover {
  color: #fff !important;   /* 원하는 색상으로 고정 */
  transition: color 0.15s;
}
</style>
