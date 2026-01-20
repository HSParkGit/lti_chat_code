import { courseSectionServices } from '@/services/courseSectionServices';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const useUpdateSection = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data) => courseSectionServices.updateCourseSection(data),
    onSuccess: () => {
      queryClient.invalidateQueries(['getCourseSections']);
    },
  });
};

export default useUpdateSection;
