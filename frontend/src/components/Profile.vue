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

    <!-- 피드 그리드 -->
    <section class="profile-feed-2025">
      <div class="feed-grid-2025">
        <div>
          <div v-for="photo in photos" :key="photo.id">
            <img :src="photo.image_url" alt="게시물 이미지" />
            <p>{{ photo.caption }}</p>
            <small>{{ photo.created_at }}</small>
          </div>
        </div>
      </div>
    </section>
  </div>

  <!-- 팔로워 목록 모달 -->
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

  <!-- 팔로잉 목록 모달 -->
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
</template>



<script setup>

import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import api from "../api/axios.js";
import {useUserStore} from "../stores/auth.js";

const showFollowers = ref(false);
const showFollowings = ref(false);
const isMe = ref(false);
const isFollowing = ref(false);
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
    api.get('/auth/photo', { headers: { Authorization: `Bearer ${token}` } })
  ]);
  userStore.setUser(profileRes.data);
  user.value = profileRes.data;
  photos.value = photoRes.data;
}

async function handleUserProfile(userId) {
  const [profileRes, photoRes] = await Promise.all([
    api.get(`/auth/profile/${userId}`),
    api.get(`/auth/photo/${userId}`)
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

/* 프로필 상단 */
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
</style>
