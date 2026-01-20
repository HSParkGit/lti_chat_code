import { ApiError } from '../utils/errors';
import apiService from '../api/apiService';
import { get } from 'jquery';

export const userService = {
  getUsers: async (termId = undefined) => {
    try {
      // if (!termId || !Number.isInteger(Number(termId))) {
      //   throw new ApiError('Valid Term ID is required');
      // }
      let url = '/users?size=5000';
      if (termId) {
        url += `&termId=${termId}`;
      }
      const response = await apiService.get(url);
      return response?.data?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch users data: ${error.message}`);
    }
  },
  searchUsers: async (courseId, sectionId) => {
    try {
      const params = new URLSearchParams();
      if (courseId) params.append('course_id', courseId);
      if (sectionId) params.append('section_id', sectionId);

      const url = `/chat/users/search?keyword=&type=student${params.size > 0 ? `&${params.toString()}` : ''}`;
      const response = await apiService.get(url);
      return response?.data?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch users data: ${error.message}`);
    }
  },
  getUserById: async (userNumber) => {
    try {
      if (!userNumber) {
        throw new ApiError('User number is required');
      }
      const response = await apiService.get(`/users/${userNumber}`);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch user details: ${error.message}`);
    }
  },

  updateUser: async ({ userNumber, updateData }) => {
    try {
      if (!userNumber) {
        throw new ApiError('User number is required');
      }
      const response = await apiService.put(`/users/${userNumber}`, updateData);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to update user: ${error.message}`);
    }
  },

  updatePassword: async (updatedData) => {
    try {
      let url = '/users/password';
      if (updatedData.oldPassword === '') {
        url = 'user/password/reset';
      }
      const response = await apiService.patch(url, updatedData);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to update password: ${error.message}`);
    }
  },

  deleteUser: async (userNumber) => {
    try {
      if (!userNumber) {
        throw new ApiError('User number is required');
      }

      const response = await apiService.delete(`/users/${userNumber}`);

      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to delete user: ${error.message}`);
    }
  },

  createUser: async (userData) => {
    try {
      const response = await apiService.post('/users/create', userData);

      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to create user: ${error.message}`);
    }
  },

  createGroup: async (data) => {
    try {
      const response = await apiService.post('/system-groups/create', data);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to create group: ${error.message}`);
    }
  },

  getInvitations: async (userId) => {
    try {
      const response = await apiService.get(`/users/${userId}/enrollments/invite`);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch invitations: ${error.message}`);
    }
  },
  getUserSettings: async (userId) => {
    try {
      const response = await apiService.get(`/users/${userId}/settings`);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch user settings: ${error.message}`);
    }
  },
  updateDiscussionSettings: async (userId, data) => {
    try {
      const response = await apiService.put(`/users/${userId}/settings`, data);
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to update discussion settings: ${error.message}`);
    }
  },
  acceptInvitation: async (invitationId, courseId, userId) => {
    try {
      const response = await apiService.post(
        `/courses/${courseId}/enrollments/${invitationId}/accept?as_user_id=${userId}`
      );
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to accept invitation: ${error.message}`);
    }
  },
  declineInvitation: async (invitationId, courseId, userId) => {
    try {
      const response = await apiService.post(
        `/courses/${courseId}/enrollments/${invitationId}/reject?as_user_id=${userId}`
      );
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to decline invitation: ${error.message}`);
    }
  },
};
