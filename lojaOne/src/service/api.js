import axios from 'axios';

// URLs de API
const localApiUrl = 'http://localhost:8080/academico/api/v1/';
const networkApiUrl = 'http://192.168.0.8:8080/academico/api/v1/';

const api = axios.create({
  baseURL: networkApiUrl
});

export const setAuthToken = (token) => {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  } else {
    delete api.defaults.headers.common['Authorization'];
  }
};

export default api;