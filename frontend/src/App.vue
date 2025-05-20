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
body {
  background: #000 !important;
}
</style>
