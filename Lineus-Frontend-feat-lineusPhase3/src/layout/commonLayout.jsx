import { Outlet } from 'react-router-dom';
import { AutoSideBar } from '../components/common/SideBar/auto';
import useSideBar from '../hooks/useSideBar';
import { RxHamburgerMenu } from 'react-icons/rx';
import { useEffect, useState } from 'react';
import { Button } from '@headlessui/react';
import { useGetInvitation, useGetUserSettings } from '@/hooks/common/useUser.js';
import { useUserStore } from '@/store/useUserStore.js';

const CommonLayout = ({ NavBar }) => {
  const { links, footer } = useSideBar();
  const { user, setInvitations, setUserSettings } = useUserStore();

  const [isSideBarOpen, setIsSideBarOpen] = useState(true);

  const toggleSideBar = () => {
    setIsSideBarOpen(!isSideBarOpen);
  };
  const { data: invitations, refetch: invitationRefetch } = useGetInvitation(user.lmsUserId);
  const { data: userSettings, refetch: userSettingsRefetch } = useGetUserSettings(user.lmsUserId);
  useEffect(() => {
    if (userSettings) {
      setUserSettings(userSettings);
    }
  }, [userSettings]);

  useEffect(() => {
    if (invitations?.length > 0) {
      setInvitations(invitations);
    }
  }, [invitations]);

  return (
    <div className='fixed flex h-full w-full overflow-y-hidden bg-white'>
      {/* <div className='hidden h-full overflow-hidden sm:flex'>
        {isSideBarOpen && <AutoSideBar items={links} footer={footer} />}
      </div> */}

      <div className='flex h-full w-full flex-1 flex-col overflow-hidden'>
        {/* <span className='relative flex-shrink-0'>
          <Button onClick={toggleSideBar} className='absolute left-3 top-5'>
            <RxHamburgerMenu color='#BBB' />
          </Button>
          <NavBar />
        </span> */}
        <main className='max-h-full w-full flex-1 overflow-hidden overflow-y-auto bg-white'>
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default CommonLayout;
