import { useMutation, useQuery } from '@tanstack/react-query';
import { useCallback } from 'react';
import service from '../../api/apiService';
import { chatServices } from '../../services/chatServices';

export const useGroup = () => {
  const fetchGroup = useCallback(async () => {
    const { data } = await service.get(`groups`);
    if (data?.data) return data?.data;
  }, []);

  const { data, isLoading, error } = useQuery({
    queryKey: ['getGroup'],
    queryFn: fetchGroup,
  });

  return {
    data,
    isLoading,
    error,
  };
};

export const useLeaveGroup = () => {
  return useMutation({
    mutationFn: (id) => chatServices().leaveGroup(id),
  });
};
