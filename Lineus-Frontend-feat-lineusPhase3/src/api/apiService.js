import axios from 'axios';
import { useUserStore } from '../store/useUserStore';
import { ApiError } from '../utils/errors';

const apiVer2 = import.meta.env.VITE_API_V2_KEY;
const apiVer1 = import.meta.env.VITE_API_KEY;
const service = axios.create({
  baseURL: apiVer1,
});

service.interceptors.request.use(
  (config) => {
    // Get fresh token for each request
    const token = useUserStore.getState().user?.accessToken;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
service.interceptors.response.use(
  async (response) => {
    // if (response.data.errorCode) {
    //   window.location.href = '/login';
    // }

    //need to implement later

    if (
      response.data.errorCode === 'T005' ||
      response.data.errorCode === 'T003' ||
      response.data.errorCode === 'T007'
    ) {
      // useUserStore.getState().removeAll();
      // // localStorage.clear();
      // // sessionStorage.clear();
      // // // window.location.replace('/login?sessionExpired=true');
      //implement later
      // try {
      //   const res = await axios.post(`${apiVer2}/reissue`, {
      //     refreshToken: getCookie('refreshToken'),
      //     accessToken: getSessionStorage('accessToken'),
      //   });
      //   console.log('call');
      //   if (res.data?.errorCode) {
      //     // window.location.href = '/login';
      //   }
      //   const newAccessToken = res.data.accessToken;
      //   setSessionStorage('accessToken', 'Helll');
      //   service.defaults.headers.Authorization = `Bearer ${newAccessToken}`;
      // } catch (error) {
      //   console.log('Erro', error);
      // }
    } else if (response.data?.errorCode === 'U004') {
      // throw new Error('Something went wrong. Please try again (or) Contact Support.');
      // alert('Something went wrong. Please try again.');
      // window.location.href = '/dashboard';
      // throw new Error(response?.data?.errorMessage);
    } else if (response.data?.errorCode === 'CR003') {
      // alert('Something went wrong. Please try again.');
      return response;
      // window.location.href = '/dashboard';
    } else if (response.data?.errorCode) {
      throw new ApiError(response.data?.errorMessage);
    }

    return response;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default service;
