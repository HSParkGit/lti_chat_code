'use client';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

import { CalendarActions } from '../../../components/Calendar/CalendarActions'; // Adjust the import path as needed
import BigCalendar from '../../../components/common/BigCalendar';
import { TermSelectBox } from '../../../components/common/TermSelectBox';
import CourseGridView from '../../../components/Students/Course/CourseGridView';
import { useCourseList } from '../../../hooks/student/useCourse';

import Loader from '../../../components/Loader';
import { QuickMessageForSidebar } from '../../../components/QuickMessages/QuickMessageSidebar';
import RightSidebarTitle from '../../../components/RightSidebar/Title';
import CourseViewToggle from '../../../components/Students/Course/CoursesViewToggle';
import { TodoListForSidebar } from '../../../components/Todo/TodoListSidebar';
import UpcomingTests from '../../../components/UpcomingTest/UpcomingTests';
import { useTodo } from '../../../hooks/student/useTodo';
import { useGetCalendar } from '../../../hooks/useCalendar';
import useLatestAssignment from '../../../hooks/useLatestAssignment';
import { useQuickMessage } from '../../../hooks/useQuickMessages';
import { useTermStore } from '../../../store/useTermStore';
import { useUserStore } from '../../../store/useUserStore';
import { useGetInvitation } from '@/hooks/common/useUser.js';
import InviteNoti from '@components/common/InviteNotification.jsx';
import { useGetUserSettings } from '../../../hooks/common/useUser';

const Index = () => {
  const { user, invitations } = useUserStore();
  const { t } = useTranslation();
  const { data: courseData, isLoading } = useCourseList(user?.lmsUserId);
  const { data: todos = [], isLoading: isTodoLoading } = useTodo();
  const { data: quickMessages = [] } = useQuickMessage();
  const [viewType, setViewType] = useState('grid');
  const [activeSidebar, setActiveSidebar] = useState(null);
  const { selectedTerm } = useTermStore();

  const {
    data: upcomingTests,
    error: upComingError,
    isLoading: isUpcomingLoading,
  } = useLatestAssignment(selectedTerm?.id);
  const { refetch: invitationRefetch } = useGetInvitation(user?.lmsUserId);
  const {
    eventsWithColor,
    queryParams,
    setQueryParams,
    getCalendarEvents,
    createCalendarEvent,
    updateCalendarEvent,
    deleteCalEvent,
  } = CalendarActions();

  useEffect(() => {
    if (courseData?.content && courseData?.content.length > 0) {
      setQueryParams((prevParams) => ({
        ...prevParams,
        contextCodes: courseData.map((course) => `course_${course.id}`).join(','),
      }));
    }
  }, [courseData]);

  const { data: events, refetch } = useGetCalendar(queryParams);

  const handleViewToggleChange = (courseViewType) => {
    setViewType(courseViewType.value);
  };

  const handleChange = (type) => {
    if (activeSidebar === type) {
      setActiveSidebar(null);
    } else {
      setActiveSidebar(type);
    }
  };
  return (
    <>
      {invitations?.length > 0 && (
        <>
          {invitations.map((invitation) => (
            <InviteNoti key={invitation?.id} invitation={invitation} refetch={invitationRefetch} />
          ))}
        </>
      )}
      <div className='bg-[#F8FAFC] p-8'>
        <h1 className='text-xl font-bold text-[#1E293B]'>{t('dashboard_overview')}</h1>
        <p className='text-sm text-[#475569]'>{t('dashboard_desc')}</p>
      </div>

      <div className='mt-2 grid grid-cols-12 gap-4 px-5 pt-2'>
        <div className='col-span-8'>
          <div className='flex items-center space-x-4'>
            <h1 className='text-xl font-bold text-[#1E293B]'>{t('courses')}</h1>
            <TermSelectBox />
          </div>
          <div className='mt-1 inline-flex text-sm'>
            <CourseViewToggle viewType={viewType} onViewChange={handleViewToggleChange} />
          </div>

          <div className='mt-5'>
            {isLoading && <Loader />}
            <CourseGridView courses={courseData} view={viewType} />
          </div>

          <div className='mt-5'>
            <div className='bigCalendar h-[50vh] w-full text-xs'>
              <BigCalendar
                isHideToolbar={true}
                events={events?.length > 0 ? eventsWithColor(events, courseData) : []}
                // {...(getMyRole() === USER_ROLE.PROFESSOR && { professorCalendar: true, disableCreate: false })}
                createCalendarEvent={(data) => createCalendarEvent(data, refetch)}
                updateCalendarEvent={(data) => updateCalendarEvent(data, refetch)}
                deleteCalendarEvent={(data) => deleteCalEvent(data, refetch)}
              />
            </div>
          </div>
        </div>
        <div className='col-span-4 border-l pl-4'>
          <div>
            <TodoListForSidebar todos={todos} isLoading={isTodoLoading} onClose={() => setActiveSidebar(null)} />
            <RightSidebarTitle
              title={t('up_coming_test')}
              subtitle={t('up_coming_test_desc')}
              // need to implement view all button action
              handleClick={() => {}}
              badge={`${upcomingTests?.length || 0} Total`}
              isHideViewAll={true}
              viewAll={false}
            />
            <UpcomingTests data={upcomingTests} isLoading={isUpcomingLoading} error={upComingError} />
            <QuickMessageForSidebar quickMessages={quickMessages} onClose={() => setActiveSidebar(null)} />
          </div>
        </div>
      </div>
    </>
  );
};

export default Index;
