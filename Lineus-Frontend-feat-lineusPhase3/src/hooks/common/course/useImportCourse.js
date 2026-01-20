import { CourseSettingServices } from '@/services/courseSettingServices';
import { useQuery } from '@tanstack/react-query';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import toast from 'react-hot-toast';

export const useImportCourse = (courseId) => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['importCourse', courseId],
    queryFn: async () => {
      if (!courseId) throw new Error('Course ID is required');
      return CourseSettingServices.getImportCourse(courseId);
    },
    refetchInterval: (data) => {
      const isInProgress = data?.state?.data?.some(
        (item) => item?.workflowState === 'pre_processing' || item?.workflowState === 'running'
      );
      return isInProgress ? 5000 : false;
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

export const useImportCourseCreate = (courseId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ courseId, data }) => CourseSettingServices.importCourse(courseId, data),
    onSuccess: () => {
      queryClient.invalidateQueries(['importCourse', courseId]);
      toast.success('Course imported successfully');
    },
  });
};

export const useFolders = (courseId) => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['getFolders', courseId],
    queryFn: async () => {
      if (!courseId) throw new Error('Course ID is required');
      return CourseSettingServices.getFolders(courseId);
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

export const useSelectiveContents = (courseId, migrationId) => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['selectiveContents', courseId, migrationId],
    queryFn: async () => {
      return CourseSettingServices.getSelectiveContents(courseId, migrationId);
    },
    onError: (error) => {
      console.error('Failed to export course:', error);
    },
    enabled: !!courseId && !!migrationId,
  });

  return {
    data,
    isLoading,
    error,
  };
};

export const useUpdateCourseContents = (courseId) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ courseId, migrationId, data }) =>
      CourseSettingServices.updateSelectiveContents(courseId, migrationId, data),
    onSuccess: () => {
      queryClient.invalidateQueries(['importCourse', courseId]);
    },
  });
};

export const useMigrationIssues = (courseId, migrationId, enable) => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['migrationIssues', courseId, migrationId],
    queryFn: async () => {
      return CourseSettingServices.getMigrationIssues(courseId, migrationId);
    },
    onError: (error) => {
      console.error('Failed to export course:', error);
    },
    enabled: !!courseId && !!migrationId && enable,
  });

  return {
    data,
    isLoading,
    error,
  };
};

export const useProgress = (migrationId) => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['migrationProgress', migrationId],
    queryFn: async () => {
      return CourseSettingServices.getProgress(migrationId);
    },
    refetchInterval: (data) => {
      console.log(data);
      const isInProgress =
        data?.state?.data?.workflowState === 'pre_processing' ||
        data?.state?.data?.workflowState === 'running' ||
        data?.state?.data?.workflowState === 'queued';
      return isInProgress ? 5000 : false;
    },
    onError: (error) => {
      console.error('Failed to export course:', error);
    },
    enabled: !!migrationId,
  });

  return {
    data,
    isLoading,
    error,
  };
};
