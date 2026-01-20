import { useQuery } from '@tanstack/react-query';
import { QUERY_KEYS } from '../../../config/constants/queryKey';
import { getGroupMembers } from '../../../services/groupServices';

export const useGroupMembers = (groupId) => {
  const { data, ...res } = useQuery({
    queryKey: [QUERY_KEYS.GROUP_MEMBERS, groupId],
    queryFn: () => getGroupMembers(groupId),
    enabled: !!groupId,
  });

  return {
    data: data?.data ?? [],
    ...res,
  };
};
