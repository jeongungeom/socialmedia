import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import {createRouterMatcher as path} from "vue-router";

export default defineConfig({
  plugins: [vue()],

  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Spring Boot 서버 주소
        changeOrigin: true,
      },
      '/uploads': 'http://localhost:8080'
    }
  }
})
