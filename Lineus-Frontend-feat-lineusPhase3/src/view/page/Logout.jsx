import { useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Loader from '../../components/Loader';
import { delay, 삼초딜레이, 쿠키도메인 } from '../../constant';
import { removeCookies } from '../../libs/utils/webstorage/cookie';
import { getSessionStorage, removeSessionStorage } from '../../libs/utils/webstorage/session';
import { useUserStore } from '@/store/useUserStore.js';

export const Logout = () => {
  const queryClient = useQueryClient();

  const navigate = useNavigate();

  const userRefresh = async () => {
    const option = {
      path: '/',
      domain: 쿠키도메인,
    };
    queryClient.clear();
    const cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i];
      const eqPos = cookie.indexOf('=');
      const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;

      document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/' + 쿠키도메인;
    }

    const access = getSessionStorage('accessToken');
    const refresh = getSessionStorage('refreshToken');

    try {
      await axios.patch(
        `${import.meta.env.VITE_API_KEY}/logout`,
        {},
        {
          headers: { Authorization: `Bearer ${access}`, Refresh: refresh },
        }
      );

      removeSessionStorage('accessToken');
      removeCookies('refreshToken');
      removeCookies('bG1zQ291cnNlVXJs', option);
      removeCookies('bG9naW5JZA', option);
      useUserStore.getState().removeAll();
      await delay(삼초딜레이);

      navigate('/');
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    userRefresh();
  }, []);

  return <Loader />;
};

const userRefresh = async () => {
  const queryClient = useQueryClient();

  const navigate = useNavigate();

  queryClient.clear();
  const cookies = document.cookie.split(';');

  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i];
    const eqPos = cookie.indexOf('=');
    const name = eqPos > -1 ? cookie.substr(0, eqPos).trim() : cookie.trim();
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; domain=${window.location.hostname}`;
  }

  const access = getSessionStorage('accessToken');
  const refresh = getSessionStorage('refreshToken');

  try {
    await axios.patch(
      `${import.meta.env.VITE_API_KEY}/logout`,
      {},
      {
        headers: { Authorization: `Bearer ${access}`, Refresh: refresh },
      }
    );

    removeSessionStorage('accessToken');
    removeCookies('refreshToken');
    removeCookies('bG1zQ291cnNlVXJs');
    removeCookies('bG9naW5JZA');

    navigate('/');
  } catch (error) {
    console.error(error);
  }
};
