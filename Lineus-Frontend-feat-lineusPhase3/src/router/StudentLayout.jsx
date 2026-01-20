import React from 'react';
import { Outlet } from 'react-router-dom';
import { Footer, Navbar } from '../components';
import Student from '../components/Sidebar/Student';
import { useUserStore } from '../libs/store/useUserStore';

function StudentLayout() {
  const user = useUserStore((state) => state.user);
  return (
    <div className='flex flex-col items-start justify-start w-full'>
      <Navbar />
      <div className={`flex h-full ${user || user ? 'fixed' : 'relative'}`}>
        <div className='min-w-[282px] max-w-[282px] h-[calc(100vh-65px)]  bg-white border-r border-slate-50 drop-shadow-lg  overflow-auto sticky lg:flex hidden'>
          <Student />
        </div>
        <div
          className={`w-full min-h-screen flex flex-col justify-between  bg-[#F4F6F7] lg:px-[24px] px-5 py-[18px] relative`}
        >
          <div className='relative w-full h-full pb-6 overflow-x-auto'>
            <div className='flex bg-[#F4F4F4] h-full pb-[100px]'>
              <Outlet />
            </div>
          </div>
          <Footer />
        </div>
      </div>
    </div>
  );
}

export default StudentLayout;
