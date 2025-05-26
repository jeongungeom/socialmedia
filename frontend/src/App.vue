<template>
  <div id="app" :class="{'has-sidebar': !isHideSidebarPage}">
    <Sidebar v-if="!isHideSidebarPage" />
    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from "./components/Sidebar.vue"

const route = useRoute()

const hideSidebarPaths = ['/', '/signup', '/signUp']

const isHideSidebarPage = computed(() => hideSidebarPaths.includes(route.path))
</script>

<style scoped>
html, body, #app {
  background: #000 !important;
  min-height: 100vh;
  margin: 0;
  padding: 0;
}
</style>

