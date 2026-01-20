import { CourseSettingServices } from '@/services/courseSettingServices';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import toast from 'react-hot-toast';

export const useExportCourse = (courseId) => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['exportCourse', courseId],
    queryFn: async () => {
      if (!courseId) throw new Error('Course ID is required');
      return CourseSettingServices.getExportCourse(courseId);
    },
    onError: (error) => {
      console.error('Failed to export course:', error);
    },
  });

  return {
    data,
    isLoading,
    error,
  };
};

export const useExportCourseCreate = (options) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ courseId, data }) => CourseSettingServices.exportCourse(courseId, data),
    ...options,
  });
};

export const useExportCourseProgress = (courseId, exportId) => {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['export-progress', exportId],
    queryFn: () => CourseSettingServices.exportProgress(courseId, exportId),
    refetchInterval: (data) => (data.state.data?.workflowState !== 'exported' ? 1000 : false),
    enabled: !!exportId,
  });
  return {
    data,
    isLoading,
    isError,
  };
};
