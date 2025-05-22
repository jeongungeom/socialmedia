<template>
  <div class="ig-profile-2025-dark">
    <!-- 프로필 상단 -->
    <section class="profile-header-2025">
      <img :src="`/api/auth/image/${user.profilePicture}`" alt="프로필" class="profile-avatar-2025" />
      <div class="profile-meta-2025">
        <div class="profile-row-2025">
          <span class="profile-username-2025">{{ user.username }}</span>
          <button v-if="isMe" class="profile-edit-2025" @click="goUpdate">프로필 편집</button>
          <button v-if="isMe" class="profile-logout-2025" @click="logout" title="로그아웃">
            <i class="bi bi-box-arrow-right"></i>
            <span class="d-none d-md-inline">로그아웃</span>
          </button>
        </div>
        <div class="profile-stats-2025">
          <div>
            <span class="profile-label-2025">게시물 </span>
            <span class="profile-count-2025">{{ user.postCount }}</span>
          </div>
          <div>
            <span class="profile-label-2025">팔로워 </span>
            <span @click="getFollowers" style="cursor: pointer" class="profile-count-2025">{{ user.followerCount }}</span>
          </div>
          <div>
            <span class="profile-label-2025">팔로잉 </span>
            <span @click="getFollowings" style="cursor: pointer" class="profile-count-2025">{{ user.followingCount }}</span>
          </div>
        </div>
        <div class="profile-bio-2025">
          <p>{{ user.bio }}</p>
        </div>
        <!-- 팔로우 버튼: 본인 프로필이 아닐 때만 표시 -->
        <div v-if="!isMe" class="profile-follow-btn-wrap-2025">
          <button
              class="profile-follow-btn-2025"
              :class="{ following: isFollowing }"
              @click="toggleFollow(isFollowing)"
          >
            {{ isFollowing ? '팔로우 취소' : '팔로우' }}
          </button>
        </div>
      </div>
    </section>
    <section class="profile-feed-2025">
      <div class="feed-grid-2025">
        <div v-for="photo in photos" :key="photo.id" class="feed-item-2025">
          <img :src="encodeURI(photo.imageUrl)" @click="goDetail(photo.id)" alt="게시물 이미지" />
        </div>
      </div>
    </section>
  </div>

<!-- 팔로워 모달 -->
  <div v-if="showFollowers" class="popup-overlay" @click.self="closeFollowers">
    <div class="popup-modal">
      <h4>팔로워</h4>
      <ul class="popup-list">
        <li v-for="f in follows" :key="f.followerId" class="popup-item">
          <img
              :src="`/api/auth/image/${f.followerProfilePicture}`"
              alt="프로필"
              class="popup-avatar"
              width="36"
              height="36"
          />
          <span @click="goUserProfile(f.followerId)" style="cursor:pointer" class="popup-username">{{ f.followerUsername }}</span>
        </li>
      </ul>
      <button class="popup-close-btn" @click="closeFollowers">닫기</button>
    </div>
  </div>

<!-- 팔로잉 모달 -->
  <div v-if="showFollowings" class="popup-overlay" @click.self="closeFollowings">
    <div class="popup-modal">
      <h4>팔로잉</h4>
      <ul class="popup-list">
        <li v-for="f in follows" :key="f.followingId" class="popup-item">
          <img
              :src="`/api/auth/image/${f.followingProfilePicture}`"
              alt="프로필"
              class="popup-avatar"
              width="36"
              height="36"
          />
          <span @click="goUserProfile(f.followingId)" style="cursor:pointer" class="popup-username">{{ f.followingUsername }}</span>
        </li>
      </ul>
      <button class="popup-close-btn" @click="closeFollowings">닫기</button>
    </div>
  </div>

<!-- 게시글 모달 -->
  <div v-if="showDetail" class="insta-post-modal-bg">
    <div class="insta-post-modal">
      <!-- X 닫기 버튼 -->
      <button class="close-btn" @click="closeDetail" title="닫기">
        <i class="bi bi-x-lg"></i>
      </button>
      <!-- 좌측: 게시글 이미지 -->
      <div class="insta-post-image">
        <img :src="post.imageUrl" alt="게시물 이미지" />
      </div>
      <div class="insta-post-info">
        <!-- 상단: 프로필/닉네임/옵션 -->
        <div class="insta-post-header">
          <img :src="`/api/auth/image/${post.profilePicture}`" class="profile-img" alt="프로필" />
          <span class="nickname">{{ post.username }}</span>
          <div class="option-dropdown-wrap">
            <button v-if="isMe" class="option-btn" title="더보기" @click="toggleMenu">
              <i class="bi bi-three-dots"></i>
            </button>
            <div v-if="showMenu" class="option-dropdown">
              <button @click="editPost(post.id)"><i class="bi bi-pencil"></i> 수정</button>
              <button @click="deletePost(post.id)"><i class="bi bi-trash"></i> 삭제</button>
            </div>
          </div>
        </div>
        <div class="header-divider"></div>
        <div class="insta-post-content">
          <p class="caption">
            <span class="nickname">{{ post.username }}</span>
            {{ post.caption }}
          </p>
        </div>
        <!-- 하단: 좋아요/댓글/저장 아이콘 -->
        <div class="insta-post-actions">
          <!-- 좋아요 버튼과 숫자 -->
          <button
              @click="toggleLike(post.id)"
              :class="{ liked: post.isLike }"
              class="like-btn"
          >
            <i :class="post.isLike ? 'bi-heart-fill' : 'bi-heart'"></i>
          </button>
          <span v-if="post.likeCnt > 0" class="like-count">{{ post.likeCnt }}</span>
          <button><i class="bi bi-chat"></i></button>
        </div>
        <small class="created-at">{{ post.createdAt }}</small>
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

<!-- 수정 모달 -->
  <div v-if="showEditModal" class="edit-post-modal-bg" @click.self="closeEditModal">
    <div class="edit-post-modal">
      <button class="close-btn" @click="closeEditModal" title="닫기">
        <i class="bi bi-x-lg"></i>
      </button>
      <h3 class="edit-modal-title">게시글 수정</h3>
      <form @submit.prevent="submitEdit" class="edit-post-form">
        <div class="edit-image-preview">
          <img :src="editPostData.imageUrl" alt="미리보기" />
        </div>
        <textarea
            v-model="editCaption"
            class="edit-caption-input"
            maxlength="300"
            placeholder="문구를 입력하세요 (최대 300자)"
        ></textarea>
        <button class="edit-post-btn" :disabled="isSaving" type="submit">
          {{ isSaving ? '저장 중...' : '저장하기' }}
        </button>
      </form>
    </div>
  </div>
</template>



<script setup>

import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import api from "../api/axios.js";
import {useUserStore} from "../stores/auth.js";

const showEditModal = ref(false)
const editPostData = ref({ id: null, imageUrl: '', caption: '' })
const editCaption = ref('')
const isSaving = ref(false)
const showFollowers = ref(false);
const showFollowings = ref(false);
const isMe = ref(false);
const isFollowing = ref(false);
const showDetail = ref(false);
const showMenu = ref(false)
const comment = ref('')
const msg = ref('');
const success = ref(false);
const route = useRoute();
const router = useRouter();
const userStore = useUserStore()
const user = ref({
  username: '',
  profilePicture: '',
  postCount: 0,
  followerCount: 0,
  followingCount: 0,
  bio: '',
})

const follows = ref({
  followerId: '',
  followerProfilePicture: '',
  followerUsername: '',
  followingId: '',
  followingProfilePicture: '',
  followingUsername: '',
})

const post = ref({
    id: '',
    imageUrl: '',
    profilePicture: '',
    username: '',
    caption: '',
    createdAt : '',
    likeCnt : '',
    isLike : ''
})


const photos = ref([]);

onMounted(async () => {
  try {
    if (!route.params.id) {
      isMe.value = true;
      await handleMyProfile();
    } else {
      await handleUserProfile(route.params.id);
      await isFollow(route.params.id);
    }
  } catch (error) {
    handleError(error);
  }
});

async function handleMyProfile() {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw { message: '로그인이 필요합니다.', redirect: true };
  }

  const [profileRes, photoRes] = await Promise.all([
    api.get('/auth/profile', { headers: { Authorization: `Bearer ${token}` } }),
    api.get('/auth/photos', { headers: { Authorization: `Bearer ${token}` } })
  ]);
  userStore.setUser(profileRes.data);
  user.value = profileRes.data;
  photos.value = photoRes.data;
}

async function handleUserProfile(userId) {
  const [profileRes, photoRes] = await Promise.all([
    api.get(`/auth/profile/${userId}`),
    api.get(`/auth/photos/${userId}`)
  ]);

  user.value = profileRes.data;
  photos.value = photoRes.data;
}

async function isFollow(profileId) {
  const res = await api.get('/follow/isFollowing', {
    params: { userId:  profileId },
    headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
  });

  isFollowing.value = res.data;
}

function handleError(error) {
  if (error.response?.status === 401 || error.redirect) {
    msg.value = error.message || '로그인이 필요합니다.';
    localStorage.removeItem('jwt');
    router.push('/login');
  } else {
    msg.value = error.message || '프로필 정보를 불러올 수 없습니다.';
  }
  alert(msg.value);
  success.value = false;
}

function goUpdate() {
  router.push("/update")
}

function logout() {
  localStorage.removeItem('jwt');
  router.push("/");
}

async function toggleFollow(isValid) {
  if(isValid) {
    await deleteFollow();
  } else {
    await addFollow();
  }
}

async function addFollow() {
  try {
    await api.post(`/follow/${route.params.id}`, null, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    });
    await isFollow(route.params.id);
    await handleUserProfile(route.params.id);
  } catch (e) {
    msg.value = e.response?.data || '오류 발생!'
  }
}

async function deleteFollow() {
  try {
    await api.delete(`/follow/${route.params.id}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    });
    await isFollow(route.params.id);
    await handleUserProfile(route.params.id);
  } catch (e) {
    msg.value = e.response?.data || '오류 발생!'
  }
}

async function getFollowers() {
  const userId = route.params.id ? route.params.id : userStore.id;
  try {
    const followerRes = await api.get(`/follow/followers/${userId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    });
    showFollowers.value = true
    follows.value = followerRes.data;
  } catch (e) {
    msg.value = e.response?.data || '오류 발생!'
  }
}

async function getFollowings() {
  const userId = route.params.id ? route.params.id : userStore.id;
  try {
    const followingRes = await api.get(`/follow/followings/${userId}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    });
    showFollowings.value = true
    follows.value = followingRes.data;
  } catch (e) {
    msg.value = e.response?.data || '오류 발생!'
  }
}

function closeFollowers() {
  showFollowers.value = false
}
function closeFollowings() {
  showFollowings.value = false
}

async function goUserProfile(userId) {
  showFollowers.value = false
  showFollowings.value = false
  await handleUserProfile(userId);
}

async function goDetail(photoId) {
  const userId = route.params.id ? route.params.id : userStore.id;

  try {
    const res = await api.get('/auth/photoOne', {
      params: { userId:  userId, id: photoId},
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    });
    showDetail.value = true
    post.value = res.data;
  } catch (e) {
    msg.value = e.response?.data || '오류 발생!'
  }
}

function closeDetail() {
  showDetail.value = false
}

function toggleMenu() {
  showMenu.value = !showMenu.value
}
function editPost() {
  showMenu.value = false
  editPostData.value = { ...post.value };
  editCaption.value = post.value.caption;
  showEditModal.value = true
  showDetail.value = false;
}

async function deletePost(id) {
  showMenu.value = false
  if (confirm('정말 삭제하시겠습니까?')) {
    await api.delete(`/photo/delete/${id}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('jwt')}` }
    })
    alert('삭제되었습니다!')
    showDetail.value=false;
    await handleMyProfile();
  }
}

function closeEditModal() {
  showEditModal.value = false
  editPostData.value = { id: null, imageUrl: '', caption: '' }
  editCaption.value = ''
}

// 저장(수정)
async function submitEdit() {
  if (!editCaption.value.trim()) {
    alert('내용을 입력하세요!')
    return
  }
  isSaving.value = true
  try {
    await api.put(
        `/photo/update/${editPostData.value.id}`,
        { caption: editCaption.value },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
          }
        }
    )
    alert('수정되었습니다!')
    closeEditModal()
  } catch (e) {
    alert('수정 실패!')
  } finally {
    isSaving.value = false
  }
}

async function toggleLike(photoId) {
  if (post.isLike) {
    // 좋아요 취소
    post.isLike = false
    await api.delete(`/like/deleteLike/${photoId}`, {
      headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}
    })
    await goDetail(photoId);
  } else {
    // 좋아요 추가
    post.isLike = true
    await api.post('/like/addLike', {photoId: photoId}, {
      headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}
    })
    await goDetail(photoId);
  }
}


function submitComment() {
  comment.value = ''
}
</script>

<style scoped>
.ig-profile-2025-dark {
  min-height: 100vh;
  background: #111;
  color: #fafafa;
  font-family: 'Segoe UI', 'Apple SD Gothic Neo', Arial, sans-serif;
  padding: 0;
  margin: 0;
}

.profile-header-2025 {
  display: flex;
  align-items: center;
  gap: 40px;
  padding: 48px 0 24px 0;
  max-width: 900px;
  margin: 0 auto;
  border-bottom: 1px solid #222;
}

.profile-avatar-2025 {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid #333;
  object-fit: cover;
  background: #181818;
}

.profile-meta-2025 {
  flex: 1;
}

.profile-row-2025 {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 18px;
}

.profile-username-2025 {
  font-size: 2rem;
  font-weight: 700;
  color: #fafafa;
}

.profile-edit-2025 {
  padding: 7px 22px;
  border: 1px solid #333;
  background: #181818;
  border-radius: 8px;
  font-size: 1.05rem;
  font-weight: 500;
  color: #fafafa;
  cursor: pointer;
  transition: background 0.2s;
}
.profile-edit-2025:hover {
  background: #232323;
}

.profile-stats-2025 {
  display: flex;
  gap: 36px;
  margin-bottom: 10px;
}
.profile-count-2025 {
  font-weight: 700;
  font-size: 1.1rem;
  margin-right: 3px;
}
.profile-label-2025 {
  font-size: 1rem;
  color: #aaa;
}

.profile-bio-2025 {
  margin-top: 8px;
  font-size: 1.07rem;
  color: #bbb;
}

/* 피드 그리드 */
.profile-feed-2025 {
  margin: 0 auto;
  padding: 40px 0 0 0;
  max-width: 900px;
}

.feed-grid-2025 {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}

.feed-item-2025 {
  aspect-ratio: 3 / 4;
  background: #181818;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 2px 16px rgba(0,0,0,0.18);
  transition: transform 0.18s;
}
.feed-item-2025 img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.2s;
}
.feed-item-2025 img:hover {
  transform: scale(1.04);
}

/* 모바일 반응형 */
@media (max-width: 900px) {
  .profile-header-2025, .profile-feed-2025 {
    max-width: 98vw;
    padding-left: 8px;
    padding-right: 8px;
  }
  .feed-grid-2025 {
    grid-template-columns: repeat(3, 1fr);
  }
}
@media (max-width: 600px) {
  .profile-header-2025 {
    flex-direction: column;
    align-items: flex-start;
    gap: 18px;
    padding: 32px 0 16px 0;
  }
  .feed-grid-2025 {
    grid-template-columns: repeat(2, 1fr);
    gap: 5px;
  }
  .profile-avatar-2025 {
    width: 80px;
    height: 80px;
  }
}
/* ... 기존 스타일 ... */
.profile-row-2025 {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 18px;
}
.profile-edit-2025 {
  padding: 7px 22px;
  border: 1px solid #333;
  background: #181818;
  border-radius: 8px;
  font-size: 1.05rem;
  font-weight: 500;
  color: #fafafa;
  cursor: pointer;
  transition: background 0.2s;
}
.profile-edit-2025:hover {
  background: #232323;
}

/* 로그아웃 버튼 스타일 */
.profile-logout-2025 {
  padding: 7px 18px;
  border: 1px solid #333;
  background: #232323;
  border-radius: 8px;
  font-size: 1.05rem;
  font-weight: 500;
  color: #e1306c;
  cursor: pointer;
  margin-left: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: background 0.2s, color 0.2s;
}
.profile-logout-2025:hover {
  background: #181818;
  color: #fff;
}
.profile-logout-2025 i {
  font-size: 1.1rem;
}
@media (max-width: 600px) {
  .profile-row-2025 {
    flex-wrap: wrap;
    gap: 10px;
  }
  .profile-edit-2025,
  .profile-logout-2025 {
    font-size: 0.97rem;
    padding: 6px 12px;
  }
}
.profile-follow-btn-wrap-2025 {
  margin-top: 12px;
  margin-bottom: 8px;
}
.profile-follow-btn-2025 {
  padding: 7px 28px;
  border-radius: 8px;
  background: #e1306c;
  color: #fff;
  font-weight: 600;
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.18s;
}
.profile-follow-btn-2025.following {
  background: #bbb;
  color: #222;
}
.profile-follow-btn-2025:hover {
  background: #c41e5a;
}
.popup-overlay {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.popup-modal {
  background: #fff;
  border-radius: 14px;
  padding: 28px 32px;
  min-width: 480px;   /* 기존 320px에서 키움 */
  max-height: 80vh;   /* 기존 70vh에서 키움 */
  overflow-y: auto;
  box-shadow: 0 8px 32px rgba(0,0,0,0.18);
}
.popup-list {
  list-style: none;
  padding: 0;
  margin: 0 0 14px 0;
}
.popup-item {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 12px;
}
.popup-avatar {
  border-radius: 50%;
  object-fit: cover;
  border: none;
}
.popup-username {
  font-weight: 500;
  color: #222;
}
.popup-close-btn {
  background: #e1306c;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 18px;
  font-weight: 600;
  cursor: pointer;
}
.feed-grid-2025 {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 한 줄에 3개 */
  gap: 18px;
  width: 100%;
}

.feed-item-2025 {
  aspect-ratio: 3 / 4; /* 3:4 직사각형 (2025 인스타 그리드) */
  background: #181818;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 2px 16px rgba(0,0,0,0.18);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  transition: transform 0.18s;
}

.feed-item-2025 img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 사진이 div를 꽉 채움 */
  display: block;
  transition: transform 0.2s;
}

.feed-item-2025 img:hover {
  transform: scale(1.04);
  cursor: pointer;
}

.feed-item-2025 p, .feed-item-2025 small {
  margin: 0;
  padding: 6px 10px 0 10px;
  color: #fafafa;
  background: rgba(0,0,0,0.18);
  font-size: 1rem;
  word-break: break-all;
}
.feed-item-2025 small {
  font-size: 0.93rem;
  color: #bbb;
  padding-bottom: 8px;
}

@media (max-width: 900px) {
  .feed-grid-2025 {
    grid-template-columns: repeat(2, 1fr);
  }
}
@media (max-width: 600px) {
  .feed-grid-2025 {
    grid-template-columns: 1fr;
  }
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
.comment-profile-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 6px;
  background: #222;
}
.insta-post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  /* 아래 두 줄로 점 세개를 오른쪽 끝으로 */
  justify-content: flex-start;
}

.nickname {
  font-weight: bold;
  font-size: 1.07rem;
  margin-right: auto; /* 닉네임 오른쪽에 남은 공간 모두 차지 */
}

.option-dropdown-wrap {
  margin-left: 0;
  margin-right: 0;
  margin-left: auto; /* 점 세개를 오른쪽 끝으로 밀기 */
  position: relative;
  display: flex;
  align-items: center;
}

.option-btn {
  background: none;
  border: none;
  color: #ddd;
  font-size: 1.3rem;
  cursor: pointer;
  padding: 4px 8px;
  margin-left: 0;
}

.header-divider {
  width: 100%;
  height: 1px;
  background: #2d2d2d; /* 적당한 회색 */
  margin: 12px 0 16px 0;
  border: none;
}
.option-dropdown {
  position: absolute;
  top: 36px;         /* option-btn 아래로 살짝 띄움 */
  right: 0;
  background: #232323;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.18);
  min-width: 100px;
  z-index: 20;
  display: flex;
  flex-direction: column;
  align-items: stretch;    /* 버튼이 가로로 꽉 차게 */
  padding: 4px 0;
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
  width: 100%;            /* 버튼이 드롭다운 전체 너비를 차지 */
  box-sizing: border-box;
  display: flex;
  align-items: center;
  gap: 8px;
}

.option-dropdown button:hover {
  background: #181818;
  color: #e1306c;
}
.edit-post-modal-bg {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.7);
  z-index: 4000;
  display: flex;
  align-items: center;
  justify-content: center;
}
.edit-post-modal {
  background: #181818;
  border-radius: 16px;
  box-shadow: 0 4px 32px rgba(0,0,0,0.25);
  width: 350px;
  padding: 32px 28px 28px 28px;
  color: #fff;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: stretch;
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
  padding: 0;
  line-height: 1;
}
.close-btn:hover {
  color: #e1306c;
}
.edit-modal-title {
  text-align: center;
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 18px;
}
.edit-image-preview {
  width: 100%;
  aspect-ratio: 3 / 4;
  background: #232323;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.edit-image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}
.edit-caption-input {
  width: 100%;
  min-height: 80px;
  background: #232323;
  border: none;
  border-radius: 8px;
  color: #fff;
  font-size: 1.08rem;
  padding: 12px 14px;
  resize: vertical;
  outline: none;
  box-shadow: 0 0 0 1.5px #444 inset;
  transition: box-shadow 0.16s;
  margin-bottom: 20px;
}
.edit-caption-input:focus {
  box-shadow: 0 0 0 2px #e1306c inset;
}
.edit-post-btn {
  width: 100%;
  background: linear-gradient(90deg, #e1306c 0%, #fdc468 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  font-weight: 700;
  font-size: 1.13rem;
  padding: 12px 0;
  cursor: pointer;
  transition: background 0.18s;
}
.edit-post-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #444;
}

.like-btn.liked,
.like-btn.liked .bi-heart-fill {
  color: #e1306c; /* 인스타그램 핑크 */
}
.like-btn .bi-heart {
  color: #fff;
}
.like-btn .bi-heart-fill {
  color: #e1306c;
}

.like-btn {
  background: none;
  border: none;
  color: #fff;
  font-size: 1.5rem;
  cursor: pointer;
  transition: color 0.18s;
  vertical-align: middle;
  padding: 0 2px 0 0;
}

.like-count {
  font-weight: 500;
  color: #fff;
  margin-left: -10px;
  font-size: 1.07rem;
  vertical-align: middle;
}

</style>
