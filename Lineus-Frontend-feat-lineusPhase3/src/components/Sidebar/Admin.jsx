import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useLocation } from 'react-router-dom';

const Student = () => {
  const { t } = useTranslation();
  const [active, setActive] = useState(3);
  const [dropdown, setDropdown] = useState(false);
  const date = 'YYYY년 MM월 DD일';
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

        <Link to='/dashboard/attendsetting'>
          <div
            className={`w-full cursor-pointer  py-2 flex items-center gap-[8px] px-[14px] rounded-md ${
              route.pathname.includes('/dashboard/attendsetting') ? 'bg-blue-primary' : 'bg-white'
            }`}
          >
            <div className='relative'>
              <svg
                width='14'
                height='14'
                viewBox='0 0 14 14'
                fill='none'
                xmlns='http://www.w3.org/2000/svg'
                className={`${route.pathname.includes('/dashboard/attendsetting') ? 'fill-white' : 'fill-secondary'}`}
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
              <svg
                className={`absolute left-1/2 translate-x-[-50%] bottom-[1px] ${
                  route.pathname.includes('/dashboard/attendsetting') ? 'fill-[#0054a6]' : 'fill-white'
                }`}
                xmlns='http://www.w3.org/2000/svg'
                width='8'
                height='8'
                viewBox='0 0 8 8'
                fill='none'
              >
                <g clipPath='url(#clip0_1312_9399)'>
                  <path
                    d='M4.00008 4.62484C4.48333 4.62484 4.87508 4.23309 4.87508 3.74984C4.87508 3.26659 4.48333 2.87484 4.00008 2.87484C3.51683 2.87484 3.12508 3.26659 3.12508 3.74984C3.12508 4.23309 3.51683 4.62484 4.00008 4.62484Z'
                    stroke='white'
                    strokeWidth='0.666667'
                    strokeLinecap='round'
                    strokeLinejoin='round'
                  />
                  <path
                    d='M6.15841 4.62484C6.11959 4.71281 6.10801 4.81039 6.12516 4.90501C6.14232 4.99962 6.18743 5.08693 6.25466 5.15567L6.27217 5.17317C6.3264 5.22735 6.36943 5.29168 6.39878 5.3625C6.42814 5.43331 6.44325 5.50922 6.44325 5.58588C6.44325 5.66254 6.42814 5.73845 6.39878 5.80926C6.36943 5.88008 6.3264 5.94441 6.27217 5.99859C6.21799 6.05282 6.15365 6.09585 6.08284 6.12521C6.01202 6.15456 5.93612 6.16967 5.85946 6.16967C5.7828 6.16967 5.70689 6.15456 5.63607 6.12521C5.56526 6.09585 5.50092 6.05282 5.44675 5.99859L5.42925 5.98109C5.36051 5.91385 5.2732 5.86874 5.17859 5.85159C5.08397 5.83443 4.98639 5.84601 4.89841 5.88484C4.81215 5.92181 4.73858 5.9832 4.68675 6.06145C4.63493 6.1397 4.60712 6.2314 4.60675 6.32525V6.37484C4.60675 6.52955 4.54529 6.67792 4.43589 6.78732C4.3265 6.89671 4.17812 6.95817 4.02341 6.95817C3.86871 6.95817 3.72033 6.89671 3.61094 6.78732C3.50154 6.67792 3.44008 6.52955 3.44008 6.37484V6.34859C3.43782 6.25205 3.40657 6.15842 3.3504 6.07988C3.29422 6.00133 3.21571 5.9415 3.12508 5.90817C3.03711 5.86935 2.93953 5.85776 2.84491 5.87492C2.75029 5.89208 2.66299 5.93718 2.59425 6.00442L2.57675 6.02192C2.52257 6.07616 2.45824 6.11918 2.38742 6.14854C2.31661 6.17789 2.2407 6.193 2.16404 6.193C2.08738 6.193 2.01147 6.17789 1.94066 6.14854C1.86984 6.11918 1.80551 6.07616 1.75133 6.02192C1.6971 5.96775 1.65407 5.90341 1.62471 5.83259C1.59536 5.76178 1.58025 5.68587 1.58025 5.60921C1.58025 5.53255 1.59536 5.45665 1.62471 5.38583C1.65407 5.31502 1.6971 5.25068 1.75133 5.1965L1.76883 5.179C1.83607 5.11026 1.88118 5.02296 1.89833 4.92834C1.91549 4.83373 1.90391 4.73614 1.86508 4.64817C1.82811 4.5619 1.76672 4.48833 1.68847 4.43651C1.61022 4.38469 1.51852 4.35688 1.42466 4.3565H1.37508C1.22037 4.3565 1.072 4.29505 0.962602 4.18565C0.853206 4.07625 0.791748 3.92788 0.791748 3.77317C0.791748 3.61846 0.853206 3.47009 0.962602 3.36069C1.072 3.2513 1.22037 3.18984 1.37508 3.18984H1.40133C1.49787 3.18758 1.5915 3.15633 1.67004 3.10015C1.74859 3.04398 1.80842 2.96547 1.84175 2.87484C1.88057 2.78687 1.89215 2.68928 1.875 2.59467C1.85784 2.50005 1.81274 2.41274 1.7455 2.344L1.728 2.3265C1.67376 2.27233 1.63074 2.20799 1.60138 2.13718C1.57202 2.06636 1.55691 1.99045 1.55691 1.9138C1.55691 1.83714 1.57202 1.76123 1.60138 1.69041C1.63074 1.6196 1.67376 1.55526 1.728 1.50109C1.78217 1.44685 1.84651 1.40382 1.91732 1.37447C1.98814 1.34511 2.06405 1.33 2.14071 1.33C2.21737 1.33 2.29327 1.34511 2.36409 1.37447C2.4349 1.40382 2.49924 1.44685 2.55341 1.50109L2.57091 1.51859C2.63965 1.58583 2.72696 1.63093 2.82158 1.64809C2.91619 1.66524 3.01378 1.65366 3.10175 1.61484H3.12508C3.21135 1.57786 3.28492 1.51647 3.33674 1.43822C3.38856 1.35997 3.41637 1.26828 3.41675 1.17442V1.12484C3.41675 0.970128 3.47821 0.821755 3.5876 0.712358C3.697 0.602962 3.84537 0.541504 4.00008 0.541504C4.15479 0.541504 4.30316 0.602962 4.41256 0.712358C4.52196 0.821755 4.58341 0.970128 4.58341 1.12484V1.15109C4.58379 1.24494 4.6116 1.33664 4.66342 1.41489C4.71524 1.49314 4.78882 1.55453 4.87508 1.5915C4.96305 1.63033 5.06064 1.64191 5.15525 1.62476C5.24987 1.6076 5.33718 1.56249 5.40591 1.49525L5.42341 1.47775C5.47759 1.42352 5.54193 1.38049 5.61274 1.35114C5.68356 1.32178 5.75946 1.30667 5.83612 1.30667C5.91278 1.30667 5.98869 1.32178 6.05951 1.35114C6.13032 1.38049 6.19466 1.42352 6.24883 1.47775C6.30307 1.53193 6.34609 1.59626 6.37545 1.66708C6.40481 1.7379 6.41992 1.8138 6.41992 1.89046C6.41992 1.96712 6.40481 2.04303 6.37545 2.11384C6.34609 2.18466 6.30307 2.24899 6.24883 2.30317L6.23133 2.32067C6.16409 2.38941 6.11899 2.47672 6.10183 2.57133C6.08467 2.66595 6.09626 2.76353 6.13508 2.8515V2.87484C6.17205 2.9611 6.23344 3.03468 6.3117 3.0865C6.38995 3.13832 6.48164 3.16613 6.5755 3.1665H6.62508C6.77979 3.1665 6.92816 3.22796 7.03756 3.33736C7.14696 3.44675 7.20842 3.59513 7.20842 3.74984C7.20842 3.90455 7.14696 4.05292 7.03756 4.16232C6.92816 4.27171 6.77979 4.33317 6.62508 4.33317H6.59883C6.50498 4.33355 6.41328 4.36136 6.33503 4.41318C6.25678 4.465 6.19539 4.53857 6.15841 4.62484Z'
                    stroke='white'
                    strokeWidth='0.666667'
                    strokeLinecap='round'
                    strokeLinejoin='round'
                  />
                </g>
                <defs>
                  <clipPath id='clip0_1312_9399'>
                    <rect width='7' height='7' fill='white' transform='translate(0.5 0.25)' />
                  </clipPath>
                </defs>
              </svg>
            </div>

            <span
              className={`${
                route.pathname.includes('/dashboard/attendsetting') ? 'text-white' : 'text-secondary'
              } text-base`}
            >
              {t('과목별_출결설정')}
            </span>
          </div>
        </Link>

        {import.meta.env.VITE_API_ONLINELECTURE !== 'panoto' && (
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

        <Link to='/dashboard/setting'>
          <div
            className={`w-full cursor-pointer  py-2 flex items-center gap-[8px] px-[14px] rounded-md ${
              route.pathname.includes('/dashboard/setting') ? 'bg-blue-primary' : 'bg-white'
            }`}
          >
            <svg
              width='14'
              height='14'
              viewBox='0 0 14 14'
              fill='none'
              xmlns='http://www.w3.org/2000/svg'
              className={`${route.pathname.includes('/dashboard/setting') ? 'fill-white' : 'fill-secondary'}`}
            >
              <path d='M6.125 12.25C6.125 12.25 5.25 12.25 5.25 11.375C5.25 10.5 6.125 7.875 9.625 7.875C13.125 7.875 14 10.5 14 11.375C14 12.25 13.125 12.25 13.125 12.25H6.125Z' />
              <path d='M9.625 7C11.0747 7 12.25 5.82475 12.25 4.375C12.25 2.92525 11.0747 1.75 9.625 1.75C8.17525 1.75 7 2.92525 7 4.375C7 5.82475 8.17525 7 9.625 7Z' />
              <path d='M4.56432 12.25C4.44112 12.0014 4.375 11.7067 4.375 11.375C4.375 10.189 4.96917 8.96923 6.06894 8.11997C5.58783 7.96608 5.02696 7.875 4.375 7.875C0.875 7.875 0 10.5 0 11.375C0 12.25 0.875 12.25 0.875 12.25H4.56432Z' />
              <path d='M3.9375 7C5.14562 7 6.125 6.02062 6.125 4.8125C6.125 3.60438 5.14562 2.625 3.9375 2.625C2.72938 2.625 1.75 3.60438 1.75 4.8125C1.75 6.02062 2.72938 7 3.9375 7Z' />
            </svg>

            <span
              className={`${route.pathname.includes('/dashboard/setting') ? 'text-white' : 'text-secondary'} text-base`}
            >
              {t('manage_user_account')}
            </span>
          </div>
        </Link>
      </div>
    </>
  );
};

export default Student;
