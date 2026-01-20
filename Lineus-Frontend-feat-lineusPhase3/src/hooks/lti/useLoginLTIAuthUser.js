import { LTIServices } from '@/services/LTIServices';
import { useMutation } from '@tanstack/react-query';

export const useLoginLTIAuthUser = () => {
  return useMutation({
    mutationFn: (data) => LTIServices.loginLTIAuthUser(data),
  });
};
