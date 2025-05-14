<template>
  <div class="ig-feed-container d-flex">
    <!-- 왼쪽 네비게이션 (2025 신규 UI) -->
    <nav class="ig-sidebar border-end p-3">
      <div class="sticky-top">
        <i class="bi bi-instagram d-block mb-5" style="font-size:2rem; color:#e1306c;"></i>

        <ul class="nav flex-column gap-3">
          <li><a href="#" class="nav-link active"><i class="bi bi-house-door me-3"></i>홈</a></li>
          <li><a href="#" class="nav-link"><i class="bi bi-search me-3"></i>검색</a></li>
          <li><a href="#" class="nav-link"><i class="bi bi-heart me-3"></i>알림</a></li>
          <li><a href="#" class="nav-link"><i class="bi bi-plus-square me-3"></i>포스팅</a></li>
          <li><a href="#" class="nav-link" @click.prevent="goProfile"><i class="bi bi-person-circle me-3"></i>프로필</a></li>
        </ul>
      </div>
    </nav>

    <!-- 메인 피드 (4:5 비율 최신 포맷) -->
    <main class="ig-main-feed flex-grow-1 p-4">
      <div class="row justify-content-center">
        <div class="col-lg-6">
          <!-- 피드 아이템 -->
          <article
              v-for="post in posts"
              :key="post.id"
              class="card mb-4 border-0 shadow-sm"
          >
            <!-- 헤더 -->
            <div class="card-header bg-white d-flex align-items-center">
              <img
                  :src="post.user.avatar"
                  class="rounded-circle me-2"
                  width="40"
                  height="40"
              >
              <strong>{{ post.user.username }}</strong>
            </div>

            <!-- 이미지 (4:5 비율) -->
            <img
                :src="post.image"
                class="card-img-top"
                alt="게시물 이미지"
                style="aspect-ratio: 4/5; object-fit: cover;"
            >

            <!-- 액션 버튼 -->
            <div class="card-body">
              <div class="d-flex gap-3 mb-3">
                <i class="bi bi-heart fs-5"></i>
                <i class="bi bi-chat fs-5"></i>
                <i class="bi bi-send fs-5"></i>
              </div>

              <!-- 게시물 내용 -->
              <p class="card-text">
                <strong>{{ post.user.username }}</strong>
                {{ post.caption }}
              </p>
              <small class="text-muted">{{ post.time }}</small>
            </div>
          </article>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
// 더미 데이터 (실제 서비스에서는 API 호출)
const posts = ref([
  {
    id: 1,
    user: {
      username: 'creative_2025',
      avatar: 'https://source.unsplash.com/random/100x100/?portrait'
    },
    image: 'https://source.unsplash.com/random/1080x1350/?nature',
    caption: '2025년 새로운 시작 🌟 #인스타그램',
    time: '2시간 전'
  },
  {
    id: 2,
    user: {
      username: 'tech_lover',
      avatar: 'https://source.unsplash.com/random/100x100/?tech'
    },
    image: 'https://source.unsplash.com/random/1080x1350/?city',
    caption: '최신 테크 트렌드 체험 중!',
    time: '5시간 전'
  }
])

function goProfile() {
  router.push("/profile")
}

</script>

<style scoped>
.ig-feed-container {
  min-height: 100vh;
  background: #121212;
  color: #ddd;
}
.ig-sidebar {
  background: rgba(20, 20, 20, 0.95);
  border-color: rgba(255, 255, 255, 0.1);
}
.ig-main-feed {
  max-width: 700px;
  margin: 0 auto;
  color: #eee;
}
.nav-link {
  color: #bbb !important;
}
.nav-link.active {
  font-weight: 600;
  color: #fff !important;
}
.card {
  background: #1e1e1e;
  border: none;
  color: #eee;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.7);
}
.card-header {
  background: transparent;
  border-bottom: none;
  color: #eee;
}
.card-text {
  color: #ddd;
}

</style>
