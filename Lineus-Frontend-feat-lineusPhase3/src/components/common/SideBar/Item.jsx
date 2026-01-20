import { IoIosLogOut } from 'react-icons/io';
import { Link, useLocation, useMatch } from 'react-router-dom';

export function LogoSection({ logo }) {
  return (
    <div className='g-white flex h-14 items-center justify-center p-3'>
      <Link to='/dashboard'>
        <img src={logo} alt='Logo' className='m-2 mx-auto ml-0 h-10' />
      </Link>
    </div>
  );
}

const extractRealPath = (path) => {
  if (!path) return '/';
  const [, first, second] = path.split('/');
  return second ? `/${first}/${second}` : `/${first}`;
};

export function SidebarItem({ icon: Icon, text, url }) {
  const externalLink = url && url.startsWith('http');
  const { pathname } = useLocation();
  const isActive = extractRealPath(url) === extractRealPath(pathname);

  return (
    <li className='list-none'>
      <Link
        to={url}
        target={externalLink ? '_blank' : ''}
        className='flex items-center gap-3 px-4 py-3 text-gray-600 hover:bg-gray-100'
      >
        {Icon && <Icon className='h-5 w-5' />}
        {isActive && <div className='h-5 w-1 rounded-full bg-blue-500' />}
        <span className={`text-[15px] ${isActive ? 'font-bold' : 'font-normal'}`}>{text}</span>
      </Link>
    </li>
  );
}

export function MenuSection({ title, items = [], footer, profile }) {
  return (
    <div className={`${footer ? 'mt-auto' : 'mb-6'}`}>
      {!footer && <p className='mb-2 px-6 text-xs font-semibold uppercase text-gray-500'>{title}</p>}
      <ul className='space-y-1'>
        {items?.map((item) => (
          <SidebarItem key={item?.text} {...item} />
        ))}
      </ul>
      {profile}
    </div>
  );
}

export function SearchBox() {
  return (
    <div className='border-t border-gray-200 px-4 py-3'>
      {/* <div className='relative'>
        <FiSearch className='absolute left-3 top-1/2 -translate-y-1/2 text-gray-400' />
        <input
          type='text'
          placeholder='Search'
          className='w-full rounded-lg border border-gray-200 bg-white py-2 pl-10 pr-9 text-sm text-gray-600 placeholder-gray-400'
        />
        <div className='absolute right-3 top-1/2 flex -translate-y-1/2 items-center gap-1 text-gray-400'>
          <FiCommand className='h-4 w-4' />
          <span className='text-xs'>K</span>
        </div>
      </div> */}
    </div>
  );
}

import { setSessionStorage } from '../../../libs/utils/webstorage/session';
import { useUserStore } from '../../../store/useUserStore';
import { useRouteStore } from '@/store/useRouteStore.js';

export function UserProfile({ user, onClick }) {
  const { removeAll } = useUserStore();
  const { clearRouteHistory } = useRouteStore();
  const handleLogout = () => {
    // Clear Zustand user store
    removeAll();
    clearRouteHistory();

    // Clear persisted Zustand data
    localStorage.removeItem('user-storage');

    // Clear all local/session storage
    // localStorage.clear();
    sessionStorage.clear();

    // Broadcast logout to other tabs
    localStorage.setItem('user-logout', Date.now().toString());

    // Redirect
    window.location.href = '/login';
  };
  return (
    <div className='border-t border-gray-200'>
      <div className='space-between flex items-center justify-between'>
        <div className='cursor-pointer p-4' onClick={onClick}>
          <div className='flex items-center space-x-3'>
            <div className='h-8 w-8 overflow-hidden rounded-full'>
              <img
                src={user?.profileImage || 'https://via.placeholder.com/32'}
                alt='Profile'
                className='h-full w-full object-cover'
              />
            </div>
            <div className='w-20'>
              <p className='text-sm font-medium text-gray-700'>{user?.userNumber || 'Loading...'}</p>
              <p className='text-xs text-gray-500'>{user?.email || 'loading...'}</p>
            </div>
          </div>
        </div>
        <div className='mb-1.5'>
          <button className='pr-3 text-xs text-gray-500 hover:text-gray-600' onClick={handleLogout}>
            <IoIosLogOut className='h-4 w-4' />
          </button>
        </div>
      </div>
    </div>
  );
}
