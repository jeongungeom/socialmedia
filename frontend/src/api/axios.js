import axios from 'axios';

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: '/api', // 백엔드 주소
    withCredentials: true, // 쿠키 전송 허용
});


export default api;
