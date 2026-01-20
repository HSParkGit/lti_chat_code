import { courseSectionServices } from '@/services/courseSectionServices';
import { useMutation, useQueryClient } from '@tanstack/react-query';

const useDeleteSection = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id) => courseSectionServices.deleteCourseSection(id),
    onSuccess: () => {
      queryClient.invalidateQueries(['getCourseSections']);
    },
  });
};

export default useDeleteSection;
