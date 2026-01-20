import { CourseSettingServices } from '@/services/courseSettingServices';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const useUpdateCourseSetting = () => {
  // const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data) => CourseSettingServices.updateCourseSetting(data),
    onSuccess: () => {
      // queryClient.invalidateQueries(['getStudentCourseList']);
    },
  });
};

export default useUpdateCourseSetting;
