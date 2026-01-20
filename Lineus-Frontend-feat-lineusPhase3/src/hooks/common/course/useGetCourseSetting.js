import { CourseSettingServices } from '@/services/courseSettingServices';
import { useQuery } from '@tanstack/react-query';

const UseGetCourseSetting = (courseId) => {
  const { data } = useQuery({
    queryKey: ['getCourseSetting'],
    queryFn: async () => CourseSettingServices.getCourseSettings(courseId),
    onError: (error) => {
      console.error('Failed to fetch course list:', error);
    },
  });
  return {
    data,
  };
};

export default UseGetCourseSetting;
