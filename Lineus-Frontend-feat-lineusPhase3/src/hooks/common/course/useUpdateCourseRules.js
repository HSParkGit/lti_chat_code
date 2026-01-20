import { CourseSettingServices } from '@/services/courseSettingServices';
import { useMutation } from '@tanstack/react-query';
const useUpdateCourseRules = () => {
  return useMutation({
    mutationFn: (data) => CourseSettingServices.updateCourseRules(data),
  });
};

export default useUpdateCourseRules;
