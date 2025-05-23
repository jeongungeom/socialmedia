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
                  @click="goPage(post.userId)"
              >
              <strong>{{ post.username }}</strong>
            </div>
            <img
                :src="encodeURI(post.imageUrl)"
                class="card-img-top"
                alt="게시물 이미지"
                style="aspect-ratio: 4/5; object-fit: cover; cursor: pointer"
                @click="goDetail(post.id, post.userId, true)"
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
                    @click="goDetail(post.id, post.userId, true)"
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

  <div v-if="showDetail && photoOne.id" class="insta-post-modal-bg">
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
          <img @click="goPage(photoOne.userId)" :src="`/api/auth/image/${photoOne.profilePicture}`"  class="profile-img" alt="프로필" />
          <span @click="goPage(photoOne.userId)" class="nickname" >{{ photoOne.username }}</span>
        </div>
        <div class="header-divider"></div>
        <div class="insta-post-content">
          <ul class="caption-and-comments">
            <li class="caption-item">
              <img @click="goPage(photoOne.userId)" :src="`/api/auth/image/${photoOne.profilePicture}`" class="comment-profile-img" alt="프로필" />
              <div class="comment-content">
                <span @click="goPage(photoOne.userId)" class="comment-nickname">{{ photoOne.username }}</span>
                <span class="comment-text">{{ photoOne.caption }}</span>
              </div>
            </li>
            <li
                v-for="comment in commentList"
                :key="comment.id"
                class="comment-item"
            >
              <img :src="`/api/auth/image/${comment.profilePicture}`" class="comment-profile-img" alt="댓글 프로필" />
              <div class="comment-content">
                <span class="comment-nickname">{{ comment.username }}</span>
                <span class="comment-text">{{ comment.commentText }}</span>
                <div class="comment-date">{{ comment.createdAt }}</div>
              </div>
              <button
                  v-if="comment.userId === userStore.id"
                  class="delete-comment-btn"
                  @click="deleteComment(comment.id, photoOne.id, photoOne.userId)"
                  title="댓글 삭제"
              >
                <i class="bi bi-trash"></i>
              </button>
            </li>
          </ul>

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
        <!-- 댓글 입력 -->
        <form class="insta-post-comment-form" @submit.prevent="submitComment(photoOne.id, photoOne.userId)">
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
import {useUserStore} from "../stores/stores.js";
import { useRouter } from 'vue-router'

const userStore = useUserStore();
const showDetail = ref(false);
const comment = ref('');
const router = useRouter()
const posts = ref([

])
const photoOne = ref({
  id: '',
  userId: '',
  imageUrl: '',
  profilePicture: '',
  username: '',
  caption: '',
  createdAt: '',
  likeCnt: 0,
  isLike: false
})

const commentList =  ref([])

onMounted(async () => {
  await rendering();
});

async function rendering() {
  try {
    const res = await api.get('/photo/allPhotos')
    posts.value = res.data;
  } catch (e) {
    console.log(e.response.data);
  }
}
async function goDetail(photoId, userId, valid) {

  try {
    const res = await api.get('/auth/photoOne', {
      params: { userId:  userId, id: photoId}
    });
    if(valid) {
      showDetail.value = true
    }
    photoOne.value = res.data.photoDto;
    commentList.value = res.data.commentsList;
  } catch (e) {
    console.log(e.response);
  }
}


async function toggleLike(photoId) {
  const post = posts.value.find(p => p.id === photoId)
  if (post.isLike > 0) {
    // 좋아요 취소
    post.isLike = false
    photoOne.value.isLike = false;
    await api.delete(`/like/deleteLike/${photoId}`)
    await goDetail(photoId, post.userId, false);
    await rendering();
  } else {
    // 좋아요 추가
    post.isLike = true
    photoOne.value.isLike = true;
    await api.post('/like/addLike', { photoId: photoId })
    await goDetail(photoId, post.userId, false);
    await rendering();
  }
}

function closeDetail() {
  showDetail.value = false
}

async function submitComment(photoId, userId) {
  try {
    await api.post("/comment/insertComment",
        {
          commentText: comment.value, photoId: photoId
        });
    comment.value = ''
   await goDetail(photoId, userId, true)
  }catch (e) {
    console.log(e)
  }
}

async function deleteComment(commentId, photoId, userId) {
  if (confirm('정말 이 댓글을 삭제하시겠습니까?')) {
    await api.delete(`/comment/deleteComment/${commentId}`)
    // 댓글 삭제 후 최신 댓글 목록 다시 불러오기
    await goDetail(photoId, userId, false);
  }
}

function goPage(id) {
  router.push(`/profile/${id}`)
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
  cursor: pointer;
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
  cursor: pointer;
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
.header-divider {
  width: 100%;
  height: 1px;
  background: #2d2d2d; /* 적당한 회색 */
  margin: 12px 0 16px 0;
  border: none;
}
button .bi.bi-chat,
button .bi.bi-chat:hover {
  color: #fff !important;   /* 원하는 색상으로 고정 */
  transition: color 0.15s;
}
.comment-item {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}
.comment-profile-img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  margin-right: 8px;
  cursor: pointer;
}
.comment-nickname {
  font-weight: bold;
  margin-right: 6px;
  cursor: pointer;
}
.comment-text {
  margin-right: 8px;
}
.comment-date {
  color: #aaa;
  font-size: 0.85em;
  margin-left: auto;
}
.caption-and-comments {
  list-style: none;
  padding: 0;
  margin: 0 0 12px 0;
}
.caption-item, .comment-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
  position: relative;
}
.comment-profile-img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  margin-right: 8px;
  margin-top: 2px;
}
.comment-content {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  min-width: 0;
}
.comment-nickname {
  font-weight: bold;
  margin-right: 6px;
}
.comment-text {
  margin-right: 8px;
  word-break: break-all;
}
.comment-date {
  color: #aaa;
  font-size: 0.85em;
  margin-top: 2px;
  margin-left: 0; /* 왼쪽 정렬 */
}
.delete-comment-btn {
  background: none;
  border: none;
  color: #e1306c;
  cursor: pointer;
  font-size: 1.1em;
  opacity: 0.7;
  transition: opacity 0.2s;
  position: absolute;
  right: 0;
  top: 10px;
}
.delete-comment-btn:hover {
  opacity: 1;
  color: #ff1744;
}

</style>
