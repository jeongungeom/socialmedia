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
                  :src="post.user.avatar"
                  class="rounded-circle me-2"
                  width="40"
                  height="40"
              >
              <strong>{{ post.user.username }}</strong>
            </div>
            <img
                :src="post.image"
                class="card-img-top"
                alt="게시물 이미지"
                style="aspect-ratio: 4/5; object-fit: cover;"
            >
            <div class="card-body">
              <div class="d-flex gap-3 mb-3">
                <i class="bi bi-heart fs-5"></i>
                <i class="bi bi-chat fs-5"></i>
                <i class="bi bi-send fs-5"></i>
              </div>
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

const posts = ref([

])

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
