import service from '@/api/apiService';
import toast from 'react-hot-toast';

export const commonServices = {
  uploadFile: async (formData) => {
    try {
      const { data } = await service.post(`file`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      return data;
    } catch (error) {
      throw new Error('Failed to upload file');
    }
  },
};
