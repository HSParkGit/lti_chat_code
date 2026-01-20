import { useMutation } from '@tanstack/react-query';
import { acceptGroupInvite } from '@/services/groupServices.js';

const UseAcceptInvite = () => {
  return useMutation({
    mutationFn: (inviteCode) => acceptGroupInvite(inviteCode),
  });
};

export default UseAcceptInvite;
