import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { userService } from '../../services/userServices';

/**
 * Hook to search users
 * @param {string} searchTerm
 * @param {string} courseId
 * @param {string} sectionId
 */
export const useSearchUsers = (courseId, sectionId) => {
  return useQuery({
    queryKey: ['users'],
    queryFn: () => userService.searchUsers(courseId, sectionId),
  });
};

export const useCreateGroup = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data) => userService.createGroup(data),
    onSuccess: (data) => {
      queryClient.invalidateQueries(['users']);
      return data;
    },
    onError: (error) => {
      throw error;
    },
  });
};

export const useGetInvitation = (userId) => {
  return useQuery({
    queryKey: ['invitation'],
    queryFn: () => userService.getInvitations(userId),
  });
};

export const useUpdateDiscussionSettings = (userId) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data) => userService.updateDiscussionSettings(userId, data),
    onSuccess: (data) => {
      queryClient.invalidateQueries(['users']);
      return data;
    },
    onError: (error) => {
      throw error;
    },
  });
};

export const useGetUserSettings = (userId) => {
  return useQuery({
    queryKey: ['userSettings'],
    queryFn: () => userService.getUserSettings(userId),
  });
};

export const useAcceptInvitation = () => {
  return useMutation({
    mutationFn: (data) => {
      return userService.acceptInvitation(data.invitationId, data.courseId, data.userId);
    },
  });
};

export const useDeclineInvitation = () => {
  return useMutation({
    mutationFn: (data) => {
      return userService.declineInvitation(data.invitationId, data.courseId, data.userId);
    },
  });
};
