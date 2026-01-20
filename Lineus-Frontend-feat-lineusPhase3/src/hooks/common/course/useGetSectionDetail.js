import { CourseSettingServices } from '@/services/courseSettingServices';
import { useQuery } from '@tanstack/react-query';

const useGetSectionDetail = (sectionId) => {
  const data = useQuery({
    queryKey: ['getSectionDetail', sectionId],
    queryFn: async () => CourseSettingServices.getSectionDetail(sectionId),
    onError: (error) => {
      console.error('Failed to fetch course list:', error);
    },
  });
  return data;
};

export default useGetSectionDetail;
