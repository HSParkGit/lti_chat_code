import { CourseSettingServices } from '@/services/courseSettingServices';
import { useQuery } from '@tanstack/react-query';

const useGetAccounts = (id) => {
  const { data, error, isLoading } = useQuery({
    queryKey: ['subAccounts'],
    queryFn: () => CourseSettingServices.getUserAccount(id),
  });
  return {
    data,
    error,
    isLoading,
  };
};

export default useGetAccounts;
