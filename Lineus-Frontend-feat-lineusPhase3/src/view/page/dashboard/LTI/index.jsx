import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Chat from '../chat';
import Loader from '@/components/Loader';
import { setSessionStorage } from '@/libs/utils/webstorage/session';
import { setCookie } from '@/libs/utils/webstorage/cookie';
import service from '@/api/apiService';
import { useUserStore } from '@/store/useUserStore';
import i18n from '@/i18n/config';
import { useLangStore } from '@/store/useLangStore';
import { useLoginLTIAuthUser } from '@/hooks/lti/useLoginLTIAuthUser';

const LTIStarterPage = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const ltiUser = useLoginLTIAuthUser();

  const id = params.get('id');
  const token = params.get('token');
  const refreshToken = params.get('refresh_token');
  const courseId = params.get('course_id');
  const locale = params.get('launch_presentation_locale') || 'en';
  const setUser = useUserStore((state) => state.setUser);
  const user = useUserStore((state) => state.user);

  const { setLang } = useLangStore();

  const [isLoading, setIsLoading] = useState(true);

  // Set default language
  useEffect(() => {
    i18n.changeLanguage(locale);
    setLang(locale);
  }, [locale]);

  useEffect(() => {
    // // Save LTI params before server login
    setSessionStorage('accessToken', token);
    setCookie('refreshToken', refreshToken);
    setSessionStorage('userId', id);
    setSessionStorage('courseId', courseId || '');
    localStorage.setItem('loggedIn', 'true');
    localStorage.setItem('accessToken', token);
    setUser({
      ...user,
      ...ltiUser,
      lmsUserId: id,
      id,
      accessToken: token,
      refreshToken,
      courseId,
    });

    // Set default axios header
    service.defaults.headers['Authorization'] = `Bearer ${token}`;
    setIsLoading(false);
  }, [id]);

  return isLoading ? (
    <div className='flex h-screen items-center justify-center'>
      <Loader />
    </div>
  ) : (
    <Chat hideProjectGroup />
  );
};

export default LTIStarterPage;
