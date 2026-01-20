import { CourseSettingServices } from '@/services/courseSettingServices';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const useUpdateCoursePublish = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ courseId, formData }) => CourseSettingServices.updateCoursePublish(courseId, formData),
    onSuccess: (data) => {
      queryClient.invalidateQueries(['courseDetail']);
    },
  });
};

export default useUpdateCoursePublish;
