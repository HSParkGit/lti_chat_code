import '@fortawesome/fontawesome-free/css/all.min.css';
import { Helmet } from 'react-helmet';
import { PROJECT_Map } from './constant';
import Router from './router';
import './App.css';
import { useEffect } from 'react';
import { getCookie, removeCookies } from '@/libs/utils/webstorage/cookie.js';
import { useUserStore } from '@/store/useUserStore.js';
import { setSessionStorage } from '@/libs/utils/webstorage/session.js';

function App() {
  const projectTitle = PROJECT_Map[import.meta.env.VITE_PROJECT_TYPE].title || 'Default Title';
  const { getUserId, user } = useUserStore();
  // add session storage for access token if there is data in local storage
  useEffect(() => {
    //get token from logged in user
    if (user && getUserId() !== undefined) {
      setSessionStorage('accessToken', user.accessToken);
      setSessionStorage('userId', getUserId());
    }
  }, []);
  // effect for logging out from all tabs by removing stored data
  useEffect(() => {
    // const handleStorage = (event) => {
    //   if (event.key === 'user-logout') {
    //     // Clear Zustand store
    //     useUserStore.getState().removeAll();
    //     localStorage.removeItem('user-storage');
    //     // Clear local/cookie storage
    //     sessionStorage.clear();
    //     removeCookies('refreshToken', { path: '/' });
    //     // Optional: redirect
    //     window.location.href = '/login';
    //   }
    // };
    // // Sending to all tabs
    // window.addEventListener('storage', handleStorage);
    // return () => window.removeEventListener('storage', handleStorage);
  }, []);
  return (
    <div>
      <Helmet>
        <title>{projectTitle}</title>
      </Helmet>
      <Router />
    </div>
  );
}

export default App;
