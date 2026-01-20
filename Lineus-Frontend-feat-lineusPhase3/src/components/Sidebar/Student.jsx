import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useLocation } from 'react-router-dom';
import { PANOTO } from '../../constant';

const Student = () => {
  const { t } = useTranslation();
  const [active, setActive] = useState(1);
  const [dropdown, setDropdown] = useState(false);
  const route = useLocation();

  const openDropdown = () => {
    setDropdown(!dropdown);
  };
  return (
    <>
      <div className='w-full bg-white h-auto px-[14px] pt-[50px] flex flex-col gap-[10px]'>
        <Link to='/dashboard'>
          <div
            className={`w-full cursor-pointer  py-2 flex items-center gap-[8px] px-[14px] rounded-md ${
              route.pathname === '/dashboard' ? 'bg-blue-primary' : 'bg-white'
            }`}
          >
            <svg
              width='14'
              height='14'
              viewBox='0 0 14 14'
              fill='none'
              xmlns='http://www.w3.org/2000/svg'
              className={`${route.pathname === '/dashboard' ? 'fill-white' : 'fill-secondary'}`}
            >
              <path d='M0.875 2.1875C0.875 1.46263 1.46263 0.875 2.1875 0.875L4.8125 0.875C5.53737 0.875 6.125 1.46263 6.125 2.1875V4.8125C6.125 5.53737 5.53737 6.125 4.8125 6.125H2.1875C1.46263 6.125 0.875 5.53737 0.875 4.8125L0.875 2.1875ZM7.875 2.1875C7.875 1.46263 8.46263 0.875 9.1875 0.875L11.8125 0.875C12.5374 0.875 13.125 1.46263 13.125 2.1875V4.8125C13.125 5.53737 12.5374 6.125 11.8125 6.125H9.1875C8.46263 6.125 7.875 5.53737 7.875 4.8125V2.1875ZM0.875 9.1875C0.875 8.46263 1.46263 7.875 2.1875 7.875H4.8125C5.53737 7.875 6.125 8.46263 6.125 9.1875V11.8125C6.125 12.5374 5.53737 13.125 4.8125 13.125H2.1875C1.46263 13.125 0.875 12.5374 0.875 11.8125L0.875 9.1875ZM7.875 9.1875C7.875 8.46263 8.46263 7.875 9.1875 7.875H11.8125C12.5374 7.875 13.125 8.46263 13.125 9.1875V11.8125C13.125 12.5374 12.5374 13.125 11.8125 13.125H9.1875C8.46263 13.125 7.875 12.5374 7.875 11.8125V9.1875Z' />
            </svg>
            <span className={`${route.pathname === '/dashboard' ? 'text-white' : 'text-secondary'} text-base`}>
              {t('home')}
            </span>
          </div>
        </Link>
        <Link to='/dashboard/attendance'>
          <div
            className={`w-full cursor-pointer  py-2 flex items-center gap-[8px] px-[14px] rounded-md ${
              route.pathname.includes('/dashboard/attendance') ? 'bg-blue-primary' : 'bg-white'
            }`}
          >
            <svg
              width='14'
              height='14'
              viewBox='0 0 14 14'
              fill='none'
              xmlns='http://www.w3.org/2000/svg'
              className={`${route.pathname.includes('/dashboard/attendance') ? 'fill-white' : 'fill-secondary'}`}
            >
              <g clipPath='url(#clip0_85_14145)'>
                <rect width='14' height='14' fillOpacity='0.01' />
                <path d='M3.5 0.4375C3.5 0.195875 3.30412 0 3.0625 0C2.82088 0 2.625 0.195875 2.625 0.4375V0.875H1.75C0.783502 0.875 0 1.6585 0 2.625V3.5H14V2.625C14 1.6585 13.2165 0.875 12.25 0.875H11.375V0.4375C11.375 0.195875 11.1791 0 10.9375 0C10.6959 0 10.5 0.195875 10.5 0.4375V0.875H3.5V0.4375Z' />
                <path d='M14 12.25V4.375H0V12.25C0 13.2165 0.783502 14 1.75 14H12.25C13.2165 14 14 13.2165 14 12.25ZM9.49686 7.74686L6.87186 10.3719C6.78981 10.4539 6.67853 10.5 6.5625 10.5C6.44647 10.5 6.33519 10.4539 6.25314 10.3719L4.94064 9.05936C4.76979 8.8885 4.76979 8.6115 4.94064 8.44064C5.1115 8.26979 5.38851 8.26979 5.55936 8.44064L6.5625 9.44378L8.87814 7.12814C9.049 6.95729 9.326 6.95729 9.49686 7.12814C9.66771 7.299 9.66771 7.57601 9.49686 7.74686Z' />
              </g>
              <defs>
                <clipPath id='clip0_85_14145'>
                  <rect width='14' height='14' />
                </clipPath>
              </defs>
            </svg>

            <span
              className={`${
                route.pathname.includes('/dashboard/attendance') ? 'text-white' : 'text-secondary'
              } text-base`}
            >
              {t('online_attendence_book')}
            </span>
          </div>
        </Link>

        {import.meta.env.VITE_API_ONLINELECTURE !== PANOTO && (
          <Link to='/dashboard/zoom'>
            <div
              className={`w-full cursor-pointer  py-2 flex items-center gap-[8px] px-[14px] rounded-md ${
                route.pathname.includes('/dashboard/zoom') ? 'bg-blue-primary' : 'bg-white'
              }`}
            >
              <div className='relative'>
                <svg xmlns='http://www.w3.org/2000/svg' width='16' height='10' viewBox='0 0 16 10' fill='none'>
                  <path
                    fillRule='evenodd'
                    clipRule='evenodd'
                    d='M0 2C0 0.895431 0.89543 0 2 0H9.5C10.5158 0 11.3548 0.757325 11.483 1.73817L14.5939 0.35557C15.2551 0.0616648 16 0.545721 16 1.26938V8.73062C16 9.45428 15.2551 9.93834 14.5939 9.64443L11.483 8.26183C11.3548 9.24267 10.5158 10 9.5 10H2C0.89543 10 0 9.10457 0 8V2Z'
                    fill='white'
                    className={`${route.pathname.includes('/dashboard/zoom') ? 'fill-white' : 'fill-secondary'}`}
                  />
                </svg>
              </div>

              <span
                className={`${route.pathname.includes('/dashboard/zoom') ? 'text-white' : 'text-secondary'} text-base`}
              >
                {t('화상강의(Zoom)')}
              </span>
            </div>
          </Link>
        )}

        <div className='w-full cursor-pointer  flex flex-col items-center  gap-[10px]' onClick={openDropdown}>
          <div
            className={`flex items-center  w-full gap-[8px] justify-between  px-[14px]  py-2 rounded-md ${
              route.pathname.includes('/dashboard/notice') ||
              route.pathname.includes('/dashboard/reference') ||
              route.pathname.includes('/dashboard/manual') ||
              route.pathname.includes('/dashboard/contact')
                ? 'bg-blue-primary'
                : 'bg-white'
            }`}
            onClick={() => {
              openDropdown();
            }}
          >
            <div className='flex items-center gap-[8px] '>
              <svg
                width='14'
                height='14'
                viewBox='0 0 14 14'
                fill='none'
                xmlns='http://www.w3.org/2000/svg'
                className={
                  route.pathname.includes('/dashboard/notice') ||
                  route.pathname.includes('/dashboard/reference') ||
                  route.pathname.includes('/dashboard/manual') ||
                  route.pathname.includes('/dashboard/contact')
                    ? 'fill-white'
                    : 'fill-secondary'
                }
              >
                <path d='M14 7C14 10.866 10.866 14 7 14C3.13401 14 0 10.866 0 7C0 3.13401 3.13401 0 7 0C10.866 0 14 3.13401 14 7ZM4.80929 5.27863H5.53072C5.65153 5.27863 5.74759 5.18011 5.76382 5.0604C5.84166 4.48628 6.23569 4.06781 6.93807 4.06781C7.53792 4.06781 8.08779 4.36774 8.08779 5.08978C8.08779 5.6452 7.76009 5.9007 7.24355 6.28949C6.6548 6.71716 6.18825 7.21704 6.22158 8.02795L6.22417 8.21766C6.22581 8.33729 6.32326 8.43341 6.4429 8.43341H7.15254C7.27336 8.43341 7.37129 8.33547 7.37129 8.21466V8.12238C7.37129 7.49475 7.61013 7.31146 8.25441 6.82269C8.78761 6.41724 9.34303 5.96735 9.34303 5.02313C9.34303 3.70123 8.22664 3.0625 7.00472 3.0625C5.89602 3.0625 4.68093 3.57928 4.59822 5.06288C4.59158 5.18207 4.68992 5.27863 4.80929 5.27863ZM6.84365 10.9161C7.37685 10.9161 7.74343 10.5718 7.74343 10.1052C7.74343 9.62201 7.37685 9.2832 6.84365 9.2832C6.33266 9.2832 5.96053 9.62201 5.96053 10.1052C5.96053 10.5718 6.33266 10.9161 6.84365 10.9161Z' />
              </svg>

              <span
                className={`${
                  route.pathname.includes('/dashboard/notice') ||
                  route.pathname.includes('/dashboard/reference') ||
                  route.pathname.includes('/dashboard/manual') ||
                  route.pathname.includes('/dashboard/contact')
                    ? 'text-white'
                    : 'text-secondary'
                } text-base`}
              >
                {t('support_center')}
              </span>
            </div>
            <svg
              width='12'
              height='8'
              viewBox='0 0 12 8'
              fill='none'
              xmlns='http://www.w3.org/2000/svg'
              className={` transition-all duration-300  ${
                dropdown ||
                route.pathname.includes('/dashboard/notice') ||
                route.pathname.includes('/dashboard/reference') ||
                route.pathname.includes('/dashboard/manual') ||
                route.pathname.includes('/dashboard/contact')
                  ? 'rotate-180'
                  : 'rotate-0'
              } ${
                route.pathname.includes('/dashboard/notice') ||
                route.pathname.includes('/dashboard/reference') ||
                route.pathname.includes('/dashboard/manual') ||
                route.pathname.includes('/dashboard/contact')
                  ? 'fill-white'
                  : 'fill-secondary'
              }`}
            >
              <path
                fillRule='evenodd'
                clipRule='evenodd'
                d='M5.69015 1.06466C5.86101 0.89381 6.13802 0.89381 6.30887 1.06466L11.5589 6.31466C11.7297 6.48552 11.7297 6.76253 11.5589 6.93338C11.388 7.10424 11.111 7.10424 10.9402 6.93338L5.99951 1.99274L1.05887 6.93338C0.888017 7.10424 0.611007 7.10424 0.440153 6.93338C0.269298 6.76253 0.269298 6.48552 0.440153 6.31466L5.69015 1.06466Z'
              />
            </svg>
          </div>

          <div
            className={`${
              dropdown ||
              route.pathname.includes('/dashboard/notice') ||
              route.pathname.includes('/dashboard/reference') ||
              route.pathname.includes('/dashboard/manual') ||
              route.pathname.includes('/dashboard/contact')
                ? 'max-h-[1000px]'
                : 'max-h-0'
            }  transition-[max-height] duration-300 overflow-hidden  bg-[#EBF1FA] flex flex-col w-full  px-[14px] rounded-sm`}
          >
            <Link to='/dashboard/notice'>
              <div className='flex flex-row gap-3 items-center pl-3 py-[8px]'>
                <span
                  className={`rounded-full ${
                    route.pathname.includes('/dashboard/notice')
                      ? 'border-2 border-blue-primary w-2 h-2 '
                      : 'border border-black h-1.5 w-1.5'
                  } `}
                ></span>
                <h3
                  className={`text-[15px] ${
                    route.pathname.includes('/dashboard/notice') ? ' text-blue-primary' : ' text-secondary'
                  }`}
                >
                  {t('notice')}
                </h3>
              </div>
            </Link>
            <Link to='/dashboard/reference'>
              <div className='flex flex-row gap-3 items-center pl-3 py-[8px]'>
                <span
                  className={`rounded-full ${
                    route.pathname.includes('/dashboard/reference')
                      ? 'border-2 border-blue-primary w-2 h-2 '
                      : 'border border-black h-1.5 w-1.5'
                  } `}
                ></span>
                <h3
                  className={`text-[15px] ${
                    route.pathname.includes('/dashboard/reference') ? ' text-blue-primary' : ' text-secondary'
                  }`}
                >
                  {t('reference')}
                </h3>
              </div>
            </Link>
            <Link to='/dashboard/manual'>
              <div className='flex flex-row gap-3 items-center pl-3 py-[8px]'>
                <span
                  className={`rounded-full ${
                    route.pathname.includes('/dashboard/manual')
                      ? 'border-2 border-blue-primary w-2 h-2 '
                      : 'border border-black h-1.5 w-1.5'
                  } `}
                ></span>
                <h3
                  className={`text-[15px] ${
                    route.pathname.includes('/dashboard/manual') ? ' text-blue-primary' : ' text-secondary'
                  }`}
                >
                  {t('manual')}
                </h3>
              </div>
            </Link>
            <Link to='/dashboard/contact'>
              <div className='flex flex-row gap-3 items-center pl-3 py-[8px]'>
                <span
                  className={`rounded-full ${
                    route.pathname.includes('/dashboard/contact')
                      ? 'border-2 border-blue-primary w-2 h-2 '
                      : 'border border-black h-1.5 w-1.5'
                  } `}
                ></span>
                <h3
                  className={`text-[15px] ${
                    route.pathname.includes('/dashboard/contact') ? ' text-blue-primary' : ' text-secondary'
                  }`}
                >
                  {t('contact')}
                </h3>
              </div>
            </Link>
          </div>
        </div>
      </div>
    </>
  );
};

export default Student;
