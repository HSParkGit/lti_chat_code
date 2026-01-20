import { useTranslation } from 'react-i18next';
import { Link, useLocation, useMatch } from 'react-router-dom';
import { useUserStore } from '../../../store/useUserStore';

//import Dropdown from './Dropdown';
import NavBarProfile from './NavBarProfile';
import canvaslogo from '/assets/kumo/logo.png';
// import useSelectedCourseStore from '../../../store/useSelectedCourse';
// import useSelectedGroupStore from '../../../store/useSelectedGroup';
// import { Link, useLocation, useMatch } from 'react-router-dom';
// import { useEffect, useState } from 'react';
// import Logo from '/assets/kumo/logo.png';

const Navbar = () => {
  const { i18n, t } = useTranslation();
  const { user } = useUserStore();
  // const { data: courseList = [] } = useCourseList(user?.lmsUserId, true);
  // const { data: groupLists = [] } = useGroup();

  //const { selectedCourse, setSelectedCourse } = useSelectedCourseStore();
  //const { selectedGroup, setSelectedGroup } = useSelectedGroupStore();
  const isCoursesRoute = useMatch('/dashboard/courses');
  const isCoursesDetailRoute = useMatch('/dashboard/courses/:id');
  const isCoursesAssignmentDetailRoute = useMatch('/dashboard/course/:id/assignment/:id');
  const isGroupsRoute = useMatch('/dashboard/group');

  // const [dropdownStates, setDropdownStates] = useState({
  //   courses: false,
  //   groups: false,
  // });

  // useInitializeSelection(courseList?.content, selectedCourse, setSelectedCourse);
  // useInitializeSelection(groupLists, selectedGroup, setSelectedGroup);

  // const handleCourseSelect = (course) => {
  //   setSelectedCourse(course);
  // };

  const isCurrentCoursesRoute = isCoursesRoute || isCoursesDetailRoute || isCoursesAssignmentDetailRoute;
  const location = useLocation();

  // const getCurrentRoute = () => {
  //   const currentRoute = location.pathname;
  //   const splitRoute = currentRoute.split('/');
  //   return splitRoute[2] || 'dashboard';
  // };

  return (
    <header className='flex-shrink-0 border-b'>
      <div className='flex h-14 items-center justify-between p-2'>
        <div className='flex items-center justify-center bg-white p-3'>
          {' '}
          {/* Set height to match header */}
          <Link className='ml-5'>
            <img src={canvaslogo} alt='Logo' className='mx-auto h-4' /> {/* Adjust logo height as needed */}
          </Link>
          {/* {isCurrentCoursesRoute && (
            <Dropdown
              items={courseList.content}
              selected={selectedCourse}
              onSelect={handleCourseSelect}
              isOpen={dropdownStates.courses}
              toggle={() => setDropdownStates((s) => ({ ...s, courses: !s.courses }))}
              label={t('selectCourse')}
            />
          )} */}
          {/* {isGroupsRoute && (
            <Dropdown
              items={groupLists}
              selected={selectedGroup}
              onSelect={setSelectedGroup}
              isOpen={dropdownStates.groups}
              toggle={() => setDropdownStates((s) => ({ ...s, groups: !s.groups }))}
              label={t('selectGroup')}
            />
          )} */}
          {/* {!isCurrentCoursesRoute && !isGroupsRoute && t(getCurrentRoute())} */}
        </div>
        <NavBarProfile /> {/* Include NavBarProfile for notifications */}
      </div>
    </header>
  );
};

export default Navbar;
