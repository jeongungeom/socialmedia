<template>
  <div class="ig-profile-2025-dark">
    <!-- 프로필 상단 -->
    <section class="profile-header-2025">
      <img :src="user.profileImage" alt="프로필" class="profile-avatar-2025" />
      <div class="profile-meta-2025">
        <div class="profile-row-2025">
          <span class="profile-username-2025">{{ user.username }}</span>
          <button class="profile-edit-2025" @click="goUpdate">프로필 편집</button>
          <button class="profile-logout-2025" @click="logout" title="로그아웃">
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
            <span class="profile-count-2025">{{ user.followerCount }}</span>
          </div>
          <div>
            <span class="profile-label-2025">팔로잉 </span>
            <span class="profile-count-2025">{{ user.followingCount }}</span>
          </div>
        </div>
        <div class="profile-bio-2025">
          <p>{{ user.bio }}</p>
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
</template>

<script setup>

import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import api from "../api/axios.js";

const msg = ref('')
const success = ref(false)

const router = useRouter();
const user = ref({
  username: '',
  profilePicture: '',
  postCount: 0,
  followerCount: 0,
  followingCount: 0,
  bio: '',
})

const photos = ref([]);

onMounted(async () => {
  const token = localStorage.getItem('jwt');
  if (!token) {
    msg.value = '로그인이 필요합니다.1';
    alert(msg.value)
    await router.push('/')
    return;
  }
  try {
    await Promise.all([
      api.get('/auth/profile', {headers: {Authorization: `Bearer ${token}`}}),
      api.get('/auth/photo', {headers: {Authorization: `Bearer ${token}`}})
    ])
        .then(([profileRes, photoRes]) => {
          user.value = profileRes.data;
          photo.value = photoRes.data;
        })
  } catch (e) {
    if (e.response && e.response.status === 401) {
      msg.value = '로그인이 필요합니다.2';
      alert(msg.value)
      localStorage.removeItem('jwt');
      await router.push('/')
    } else {
      msg.value = '프로필 정보를 불러올 수 없습니다.';
    }
    success.value = false;
  }
});


function goUpdate() {
  router.push("/update")
}

function logout() {
  localStorage.removeItem('jwt');
  router.push("/");
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
</style>
