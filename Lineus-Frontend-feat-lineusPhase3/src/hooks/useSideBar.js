import { useEffect, useState } from 'react';
import { useUserStore } from '../store/useUserStore';
import { useTranslation } from 'react-i18next';
import { ChatIcon } from '../components/common/Icon';

const useSideBar = () => {
  const { t } = useTranslation();
  const { getMyRole, user } = useUserStore();
  const role = getMyRole();
  const [otherLinks, setOtherLinks] = useState([]);

  useEffect(() => {
    if (role) {
      setOtherLinks([{ icon: ChatIcon, text: t('chats'), url: `/dashboard/chat` }]);
    }
  }, [role, t, user]);

  return {
    otherLinks,
  };
};

export default useSideBar;
