import { CourseSettingServices } from '@/services/courseSettingServices';
import { useMutation, useQueryClient } from '@tanstack/react-query';
const { updateCourseImage } = CourseSettingServices;

const useUpdateCourseImage = () => {
  return useMutation({
    mutationFn: (data) => updateCourseImage(data),
    onSuccess: (data) => {
      return data;
    },
    onError: (error) => {
      return error;
    },
  });
};

export default useUpdateCourseImage;
