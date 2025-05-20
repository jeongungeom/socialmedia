<template>
  <nav class="sidebar">
    <div class="sidebar-logo mb-5">
      <i class="bi bi-instagram" style="font-size:2rem; color:#e1306c;"></i>
    </div>
    <ul class="nav flex-column gap-3">
      <li><a href="#" class="nav-link" @click="goFeed"><i class="bi bi-house-door me-3"></i>홈</a></li>
      <li>
        <a href="#" class="nav-link" @click.prevent="showSearchPanel = true">
          <i class="bi bi-search me-3"></i>검색
        </a>
      </li>
      <li><a href="#" class="nav-link"><i class="bi bi-heart me-3"></i>알림</a></li>
      <li><a href="#" class="nav-link" @click="goPost"><i class="bi bi-plus-square me-3"></i>포스팅</a></li>
      <li><a href="#" class="nav-link" @click="goProfile"><i class="bi bi-person-circle me-3"></i>프로필</a></li>
    </ul>

    <transition name="slide-left">
      <div v-if="showSearchPanel" class="ig-search-panel">
        <button class="close-btn" @click="closeSearchPanel"  title="닫기">
          <i class="bi bi-x-lg"></i>
        </button>
        <div class="search-content">
          <input
              type="text"
              class="form-control mb-3"
              placeholder="검색어를 입력하세요"
              v-model="searchQuery"
          />
          <div v-if="searchQuery">
            <div v-if="searchResults.length > 0">
              <div class="search-result" @click="goPage(user.id)" v-for="user in searchResults" :key="user.id"  style="cursor:pointer">
                <img
                    :src="`/api/auth/image/${user.profilePicture}`"
                    class="rounded-circle me-2"
                    width="32"
                    height="32"
                    alt="프로필"
                />
                <span>{{ user.username }}</span>
              </div>
            </div>
            <div v-else class="text-decoration-none">검색 결과가 없습니다.</div>
          </div>
        </div>
      </div>
    </transition>
    <div v-if="showSearchPanel" class="ig-search-overlay" @click="closeSearchPanel"></div>
  </nav>
</template>

<script setup>

import { ref, watch} from "vue";
import { useRouter } from 'vue-router'
import api from "../api/axios.js";

const router = useRouter()
const showSearchPanel = ref(false)
const searchQuery = ref('')
const searchResults = ref([])

let debounceTimer = null

watch(searchQuery, (newVal) => {
  if (!newVal) {
    searchResults.value = []
    return
  }
  if (debounceTimer) clearTimeout(debounceTimer)
  debounceTimer = setTimeout(async () => {
    try {
      const res = await api.get('/auth/searchUser',
          {
            params: {keyword: newVal},
            headers: {Authorization: `Bearer ${localStorage.getItem('jwt')}`}
          }
      )
      searchResults.value = res.data
    } catch (e) {
      searchResults.value = []
    }
  }, 400)
})

const form = ref({
  id: '',
  keyword: ''
})

function goProfile() {
  router.push('/profile');
}

function goFeed() {
  router.push('/feed');
}

function goPage(id) {
  showSearchPanel.value=false;
  router.push(`/profile/${id}`)
  searchQuery.value = '';
  searchResults.value = [];
}

function goPost() {
  router.push('/post');
}

function closeSearchPanel() {
  showSearchPanel.value = false
  searchQuery.value = ''
  searchResults.value = []
}


</script>

<style scoped>
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  width: 220px;
  height: 100vh;
  background: #181818;
  color: #fff;
  padding: 32px 20px 20px 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  box-shadow: 2px 0 16px rgba(0,0,0,0.12);
}
.sidebar-logo {
  margin-bottom: 2rem;
  text-align: left;
}
.nav-link {
  color: #bbb !important;
  font-size: 1.1rem;
  padding: 10px 0 10px 10px;
  border-radius: 8px;
  transition: background 0.18s;
  display: flex;
  align-items: center;
}
.nav-link.active,
.nav-link:hover {
  background: #232323;
  color: #fff !important;
}

/* 슬라이드 검색 패널 */
.ig-search-panel {
  position: fixed;
  top: 0; left: 0;
  width: 320px;
  height: 100vh;
  background: #232323;
  color: #fff;
  box-shadow: 4px 0 24px rgba(0,0,0,0.15);
  z-index: 2000;
  display: flex;
  flex-direction: column;
  padding: 32px 20px;
  transition: transform 0.35s cubic-bezier(.4,0,.2,1);
}
.slide-left-enter-from,
.slide-left-leave-to {
  transform: translateX(-100%);
}
.slide-left-enter-to,
.slide-left-leave-from {
  transform: translateX(0);
}
.ig-search-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0,0,0,0.25);
  z-index: 1900;
}
.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  position: absolute;
  top: 18px; right: 18px;
  color: #fff;
  cursor: pointer;
}
.search-content {
  margin-top: 48px;
}
.search-result {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #333;
}
@media (max-width: 600px) {
  .sidebar {
    width: 60px;
    padding: 20px 5px;
  }
  .ig-search-panel {
    width: 90vw;
    padding: 24px 10px;
  }
}
</style>
