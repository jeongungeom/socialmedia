<template>
  <div id="app" class="app-layout">
    <TheSidebar v-if="!isLoginPage" />
    <div class="main-content" :class="{ 'full-width': isLoginPage }">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isLoginPage = computed(() => route.path === '/login')

</script>


<style>
.app-layout {
  display: flex;
  min-height: 100vh;
}
.main-content {
  flex: 1;
  margin-left: 220px; /* 사이드바 너비만큼 여백 */
  background: #fafafa;
  min-height: 100vh;
  transition: margin-left 0.2s;
}
@media (max-width: 600px) {
  .main-content {
    margin-left: 60px;
  }
}
</style>
