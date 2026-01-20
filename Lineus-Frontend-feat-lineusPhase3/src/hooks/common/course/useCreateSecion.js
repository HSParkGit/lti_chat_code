import { courseSectionServices } from '@/services/courseSectionServices';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const useCreateSection = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data) => courseSectionServices.createCourseSection(data),
    onSuccess: () => {
      queryClient.invalidateQueries(['getCourseSections']);
    },
  });
};

export default useCreateSection;
