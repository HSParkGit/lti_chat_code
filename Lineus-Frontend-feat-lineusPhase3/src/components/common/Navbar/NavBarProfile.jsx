import ListBox from '@/components/common/Listbox';
import { faBell, faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { CANVAS_URL } from '../../../config/general';
import useNotificationCount from '../../../hooks/useNotificationCount';
import { useUserStore } from '../../../store/useUserStore';
import { getLocalStorage, setLocalStorage } from '../../../libs/utils/webstorage/local';
import { useLangStore } from '@/store/useLangStore';

const LanguageList = [
  { name: '한국어', value: 'kr' },
  { name: 'English', value: 'en' },
  { name: '中文', value: 'cn' },
];

const NavBarProfile = () => {
  const { i18n } = useTranslation();
  const { notiCount, courseNotificationCount } = useUserStore();
  const { setLang, lang } = useLangStore();
  useNotificationCount();

  const [selected, setSelected] = useState(() => {
    return LanguageList.find((lng) => lng.value === lang) || LanguageList[0];
  });

  useEffect(() => {
    if (selected) {
      // setLocalStorage('lang', selected.value);
      i18n.changeLanguage(selected.value).catch((err) => console.error('Error changing language:', err));
      setLang(selected.value);
    }
  }, [selected]);

  return (
    <div className='flex items-center gap-4'>
      {/* Language Selector */}
      <div className='flex items-center gap-2 rounded-xl border px-2 shadow-sm'>
        <svg className='h-5 w-5 text-gray-600' viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg'>
          <path
            d='M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z'
            stroke='currentColor'
            strokeWidth='1.5'
            strokeLinecap='round'
            strokeLinejoin='round'
          />
          <path d='M2 12H22' stroke='currentColor' strokeWidth='1.5' strokeLinecap='round' strokeLinejoin='round' />
          <path
            d='M12 2C14.5013 4.73835 15.9228 8.29203 16 12C15.9228 15.708 14.5013 19.2616 12 22C9.49872 19.2616 8.07725 15.708 8 12C8.07725 8.29203 9.49872 4.73835 12 2Z'
            stroke='currentColor'
            strokeWidth='1.5'
            strokeLinecap='round'
            strokeLinejoin='round'
          />
        </svg>
        <ListBox
          lists={LanguageList}
          selected={selected}
          setSelected={setSelected}
          className='border-none shadow-none'
        />
      </div>

      {/* Notification Icons */}
      <div className='flex items-center gap-3'>
        <a
          href={`${CANVAS_URL}/conversations`}
          target='_blank'
          rel='noopener noreferrer'
          className='relative rounded-lg bg-gray-50 p-2'
        >
          <FontAwesomeIcon icon={faEnvelope} className='h-5 w-5 text-gray-600' />
          {notiCount > 0 && (
            <span className='absolute -right-1 -top-1 flex h-5 w-5 items-center justify-center rounded-full bg-red-500 text-xs font-medium text-white'>
              {notiCount}
            </span>
          )}
        </a>

        <a
          href={`${CANVAS_URL}/dashboard`}
          target='_blank'
          rel='noopener noreferrer'
          className='relative rounded-lg bg-gray-50 p-2'
        >
          <FontAwesomeIcon icon={faBell} className='h-5 w-5 text-gray-600' />
          {courseNotificationCount > 0 && (
            <span className='absolute -right-1 -top-1 flex h-5 w-5 items-center justify-center rounded-full bg-red-500 text-xs font-medium text-white'>
              {courseNotificationCount}
            </span>
          )}
        </a>
      </div>
    </div>
  );
};

export default NavBarProfile;
