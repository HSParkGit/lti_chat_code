import { useMutation, useQueryClient } from '@tanstack/react-query';
import { CourseSettingServices } from '@/services/courseSettingServices';

const useRemoveUser = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data) => CourseSettingServices.removeUser(data),
    onSuccess: () => {
      queryClient.invalidateQueries(['getSectionDetail']);
    },
    onError: () => {},
  });
};

export default useRemoveUser;
