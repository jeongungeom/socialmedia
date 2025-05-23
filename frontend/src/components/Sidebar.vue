<template>
  <nav class="sidebar">
    <div class="sidebar-logo mb-5" @click="goFeed">
      <i  class="bi bi-instagram" style="font-size:2rem; color:#e1306c;"></i>
    </div>
    <ul class="nav flex-column gap-3">
      <li><a href="#" class="nav-link" @click="goFeed"><i class="bi bi-house-door me-3"></i>홈</a></li>
      <li>
        <a href="#" class="nav-link" @click.prevent="showSearchPanel = true">
          <i class="bi bi-search me-3"></i>검색
        </a>
      </li>
      <li>
        <a href="#" class="nav-link" @click="openAlarm" style="position: relative;">
    <span class="icon-wrapper">
      <i class="bi bi-heart me-3"></i>
      <span
          v-if="hasNewAlarm"
          class="alarm-badge"
          aria-label="새 알림"
      ></span>
    </span>
          알림
        </a>
      </li>
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
  <div>
    <!-- 사이드바 알림 아이콘 -->
    <button class="sidebar-icon" @click="showAlarm = true">
      <i class="bi bi-bell"></i>
    </button>

    <!-- 알람 슬라이드 패널 -->
    <transition name="slide-left">
      <div v-if="showAlarm" class="alarm-sidebar">
        <div class="alarm-header">
          <div class="alarm-title-group">
            <span>알림</span>
            <button
                class="all-read-btn"
                @click="readAll"
                :disabled="alarms.length === 0"
                title="전체 확인"
            >
              <i class="bi bi-bell-fill"></i>
            </button>
          </div>
          <button class="close-btn" @click="showAlarm = false" title="닫기">
            <i class="bi bi-x-lg"></i>
          </button>
        </div>
        <ul class="alarm-list">
          <li v-for="alarm in alarms" :key="alarm.id" class="alarm-item">
            <span class="alarm-message" @click="readNoti(alarm.id)">{{ alarm.message }}</span>
            <span class="alarm-date">{{ alarm.createdAt }}</span>
          </li>
        </ul>
        <div v-if="alarms.length === 0" class="alarm-empty">
          새로운 알림이 없습니다.
        </div>
      </div>
    </transition>
    <div v-if="showAlarm" class="alarm-overlay" @click="showAlarm = false"></div>
  </div>
</template>

<script setup>

import {computed, onMounted, ref, watch} from "vue";
import { useRouter } from 'vue-router'
import api from "../api/axios.js";
import {useUserStore} from "../stores/stores.js";

const router = useRouter()
const showSearchPanel = ref(false)
const searchQuery = ref('')
const searchResults = ref([])
const showAlarm = ref(false);
const userStore = useUserStore()
const alarms = ref([])
let eventSource = null
let debounceTimer = null

const hasNewAlarm = computed(() => alarms.value.length > 0)


onMounted(() => {
  console.log('test',userStore.id)
  if (userStore.id) {
    eventSource = new EventSource(`/api/notification/subscribe/${userStore.id}`)

    eventSource.addEventListener('alarm', (event) => {
      const data = JSON.parse(event.data)
      alarms.value.unshift({
        id: data.id,
        message: data.message,
        createdAt: new Date().toLocaleString()
      })
    })

    eventSource.onerror = () => {
      eventSource.close()
    }
  }
})



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
            params: {keyword: newVal}
          }
      )
      searchResults.value = res.data
    } catch (e) {
      searchResults.value = []
    }
  }, 400)
})

async function getAlarmList() {
  try {
    const res = await api.get('/notification/getNoti')
    alarms.value = res.data;
    showAlarm.value = true;
  } catch (e) {
    console.log(e);
  }
}

async function readNoti(notiId) {
  try {
    await api.put(`/notification/readNoti/${notiId}`)
    await getAlarmList();
    alarms.value = []
  } catch (e) {
    console.log(e);
  }
}

async function readAll() {
  try {
    await api.put('/notification/deleteAllNoti')
    await getAlarmList();
    alarms.value = []
  } catch (e) {
    console.log(e);
  }
}



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

function openAlarm() {
  showAlarm.value = true;
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
  cursor: pointer;
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
/* 알람 사이드바 */
.alarm-sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 340px;
  height: 100vh;
  background: #222;
  box-shadow: 2px 0 16px rgba(0,0,0,0.15);
  z-index: 2000;
  display: flex;
  flex-direction: column;
}

.alarm-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 18px 12px 18px;
  border-bottom: 1px solid #333;
  font-size: 1.2em;
  font-weight: bold;
  color: #fff;
}

/* 추가: 알림 글자와 종 버튼 묶음 */
.alarm-title-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.alarm-header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.all-read-btn {
  background: none;
  border: none;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2em;
  color: #ffcc33;
  cursor: pointer;
  transition: background 0.18s, color 0.18s;
  padding: 0;
}
.all-read-btn:disabled {
  color: #888;
  cursor: not-allowed;
  background: none;
}
.all-read-btn:not(:disabled):hover {
  background: #333;
  color: #ffe066;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.4em;
  cursor: pointer;
  color: #fff;
  padding: 0 2px;
  transition: color 0.18s;
}
.close-btn:hover {
  color: #ffcc33;
}

.alarm-list {
  flex: 1 1 auto;
  overflow-y: auto;
  padding: 12px 0;
  margin: 0;
  list-style: none;
}

.alarm-item {
  padding: 12px 20px;
  border-bottom: 1px solid #333;
  font-size: 1em;
  display: flex;
  flex-direction: column;
  color: #fff;
  cursor: pointer;
}
.alarm-message {
  margin-bottom: 4px;
}
.alarm-date {
  color: #ffec99;
  font-size: 0.92em;
}
.alarm-empty {
  text-align: center;
  color: #aaa;
  padding: 40px 0;
  font-size: 1.1em;
}
.alarm-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.3);
  z-index: 1999;
}
.sidebar-icon {
  background: none;
  border: none;
  font-size: 1.5em;
  cursor: pointer;
  color: #fff;
}
.icon-wrapper {
  position: relative;
  display: inline-block;
}

.alarm-badge {
  position: absolute;
  top: 0px;
  right: 11px;
  width: 10px;
  height: 10px;
  background: #ff3b3b;
  border-radius: 50%;
  border: 1.5px solid #fff;
  box-shadow: 0 0 2px rgba(0,0,0,0.15);
  display: inline-block;
  z-index: 10;
  pointer-events: none;
}

</style>
