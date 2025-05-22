import axios from 'axios';

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: '/api', // 백엔드 주소
    withCredentials: true, // 쿠키 전송 허용
});

api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('jwt');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);


export default api;
