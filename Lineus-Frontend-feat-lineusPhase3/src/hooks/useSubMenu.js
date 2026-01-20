import { useEffect, useState } from 'react';
import { useGetMenusByCourseId } from './CourseHooks';

const useSubMenu = ({ courseId }) => {
  const [subMenu, setSubMenu] = useState([]);
  const { data } = useGetMenusByCourseId(courseId);

  const convertTopicsToMenu = (name) => {
    if (name === 'Discussions') {
      return 'discussion_topics';
    }

    if (name === 'Pages') {
      return 'wiki';
    }
    if (name === 'People') {
      return 'users';
    }
    return name.toLowerCase();
  };

  const transformMenus = (menus) => {
    // const externalLinks = ['settings'];
    let finalMenus = menus.reduce((acc, menu, index) => {
      const { id, name, url, to, hidden } = menu;
      //remove external links =>    redirectUrl: externalLinks.includes(name.toLowerCase()) ? url : null,
      acc.push({
        id: acc.length + 1,
        name: `sub_menu.${name}`,
        menuKey: convertTopicsToMenu(name),
        url,
        hidden,
        redirectUrl: null,
        to: `/dashboard${to}`,
      });

      return acc;
    }, []);

    return finalMenus;
  };

  useEffect(() => {
    if (!data) {
      return;
    }
    setSubMenu(transformMenus(data));
  }, [data]);

  return {
    subMenu,
  };
};

export default useSubMenu;
