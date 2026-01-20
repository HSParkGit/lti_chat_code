import apiService from '../api/apiService';

export const LTIServices = {
  loginLTIAuthUser: async (formData) => {
    try {
      const { data, errorCode } = await apiService.post(`/authenticate-by-lms-user-id`, formData);
      if (errorCode) throw new Error('Failed to get LTI User');
      return data?.data;
    } catch (error) {
      throw new Error('Failed to get LTI User');
    }
  },
};
