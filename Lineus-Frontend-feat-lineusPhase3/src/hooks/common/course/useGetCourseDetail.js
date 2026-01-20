import { useQuery } from '@tanstack/react-query';
import { CourseSettingServices } from '@/services/courseSettingServices';

const { getCourseDetail } = CourseSettingServices;

const useGetCourseDetail = (courseId) => {
  return useQuery({
    queryKey: ['courseDetail'],
    queryFn: () => getCourseDetail(courseId),
    enabled: !!courseId,
  });
};

export default useGetCourseDetail;
