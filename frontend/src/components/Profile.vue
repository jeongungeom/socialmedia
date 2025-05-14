<template>
  <div class="ig-profile-2025">
    <!-- 상단 프로필 영역 -->
    <section class="ig-profile-header">
      <div class="ig-profile-avatar-wrap">
        <img :src="user.profileImage" alt="프로필" class="ig-profile-avatar" />
      </div>
      <div class="ig-profile-meta">
        <div class="ig-profile-row">
          <span class="ig-profile-username">{{ user.username }}</span>
          <button class="ig-profile-edit" @click="goUpdate">프로필 편집</button>
        </div>
        <div class="ig-profile-stats">
          <div>
            <span class="ig-profile-count">{{ user.postCount }}</span>
            <span class="ig-profile-label">게시물</span>
          </div>
          <div>
            <span class="ig-profile-count">{{ user.followerCount }}</span>
            <span class="ig-profile-label">팔로워</span>
          </div>
          <div>
            <span class="ig-profile-count">{{ user.followingCount }}</span>
            <span class="ig-profile-label">팔로잉</span>
          </div>
        </div>
        <div class="ig-profile-bio">
          <p>{{ user.bio }}</p>
        </div>
      </div>
    </section>

    <!-- 피드 그리드 영역 -->
    <section class="ig-profile-feed">
      <div class="ig-feed-grid">
        <div v-for="post in user.posts" :key="post.id" class="ig-feed-item">
          <img :src="post.image" alt="피드 이미지" />
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
  profileImage: '',
  postCount: 0,
  followerCount: 0,
  followingCount: 0,
  bio: '',
  posts: [
    { id: 1, image: '/feed1.jpg' },
    { id: 2, image: '/feed2.jpg' },
    // ...
  ]
})

onMounted(async () => {
  const token = localStorage.getItem('jwt');
  console.log(token);
  if (!token) {
    msg.value = '로그인이 필요합니다.1';
    alert(msg.value)
    // await router.push('/')
    return;
  }
  try {
    const res = await api.get('/auth/profile', {
      headers: { Authorization: `Bearer ${token}` }
    });
    user.value = res.data;
  } catch (e) {
    if (e.response && e.response.status === 401) {
      msg.value = '로그인이 필요합니다.2';
      alert(msg.value)
      localStorage.removeItem('jwt');
      // await router.push('/')
    } else {
      msg.value = '프로필 정보를 불러올 수 없습니다.';
    }
    success.value = false;
  }
});


function goUpdate() {
  router.push("/update")
}

</script>

<style scoped>
.ig-profile-2025 {
  max-width: 650px;
  margin: 40px auto 0 auto;
  background: var(--ig-bg, #fff);
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(0,0,0,0.07);
  padding: 32px 24px 24px 24px;
  font-family: 'Segoe UI', 'Apple SD Gothic Neo', Arial, sans-serif;
  color: var(--ig-text, #222);
}

.ig-profile-header {
  display: flex;
  align-items: flex-start;
  gap: 32px;
  margin-bottom: 32px;
}

.ig-profile-avatar-wrap {
  flex-shrink: 0;
}

.ig-profile-avatar {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  border: 3px solid #e1e1e1;
  object-fit: cover;
  background: #fafafa;
}

.ig-profile-meta {
  flex: 1;
}

.ig-profile-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.ig-profile-username {
  font-size: 1.6rem;
  font-weight: 600;
  margin-right: 8px;
}

.ig-profile-edit {
  padding: 6px 18px;
  border: 1px solid #dbdbdb;
  background: #fafafa;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  color: #222;
  cursor: pointer;
  transition: background 0.2s;
}
.ig-profile-edit:hover {
  background: #f0f0f0;
}

.ig-profile-stats {
  display: flex;
  gap: 32px;
  margin: 18px 0 6px 0;
}
.ig-profile-count {
  font-weight: 700;
  font-size: 1.2rem;
  margin-right: 3px;
}
.ig-profile-label {
  font-size: 1rem;
  color: #888;
}

.ig-profile-bio {
  margin-top: 6px;
  font-size: 1.05rem;
  color: #444;
}

.ig-profile-feed {
  margin-top: 12px;
}
.ig-feed-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}
.ig-feed-item {
  aspect-ratio: 3 / 4;
  background: #eee;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}
.ig-feed-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.2s;
}
.ig-feed-item img:hover {
  transform: scale(1.03);
}

/* 다크모드 대응 */
@media (prefers-color-scheme: dark) {
  .ig-profile-2025 {
    --ig-bg: #181818;
    --ig-text: #fafafa;
    background: var(--ig-bg);
    color: var(--ig-text);
    box-shadow: 0 4px 24px rgba(0,0,0,0.25);
  }
  .ig-profile-avatar {
    border-color: #333;
    background: #222;
  }
  .ig-profile-edit {
    background: #222;
    color: #fafafa;
    border-color: #444;
  }
  .ig-profile-edit:hover {
    background: #333;
  }
  .ig-profile-label {
    color: #bbb;
  }
  .ig-feed-item {
    background: #222;
  }
}
@media (max-width: 600px) {
  .ig-profile-2025 {
    padding: 12px 2vw;
    max-width: 100vw;
  }
  .ig-profile-header {
    flex-direction: column;
    align-items: center;
    gap: 16px;
    margin-bottom: 20px;
  }
  .ig-profile-meta {
    width: 100%;
  }
  .ig-feed-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 4px;
  }
}
</style>
