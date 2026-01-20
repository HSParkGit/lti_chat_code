import { Sidebar } from '.';
import { MenuSection, SearchBox, LogoSection, UserProfile } from './Item';
import Logo from '../../../assets/logo.svg';
import useSideBar from '../../../hooks/useSideBar';
import { useUserStore } from '../../../store/useUserStore';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export const AutoSideBar = () => {
  const { mainMenu, otherLinks, footer, marketPlace } = useSideBar();
  const { user } = useUserStore();
  const navigate = useNavigate();
  const { t } = useTranslation();

  return (
    <Sidebar>
      <LogoSection logo={Logo} />
      <SearchBox />
      {otherLinks.length > 0 && <MenuSection items={otherLinks} />}
    </Sidebar>
  );
};
