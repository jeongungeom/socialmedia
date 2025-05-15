<template>
  <div id="app" :class="{'has-sidebar': !isLoginPage}">
    <Sidebar v-if="!isLoginPage" />
    <router-view />
  </div>
</template>

<script setup>
import {computed, ref} from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from "./components/Sidebar.vue";

const route = useRoute()
const isLoginPage = computed(() => route.path === '/')

</script>


<style>
.app-layout {
  display: flex;
  min-height: 100vh;
}
.main-content {
  flex: 1;
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
