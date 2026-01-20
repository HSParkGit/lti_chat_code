import { useQuery } from '@tanstack/react-query';
import service from '../api/apiService';
import { useUserStore } from '../store/useUserStore';

const ONE_MINUTE = 1000 * 60;

const useNotificationCount = () => {
  const { setNotiCount, setCourseNotificationCount } = useUserStore();
  useQuery({
    queryKey: ['notification'],
    queryFn: async () => {
      const {
        data: { data: conversationData },
      } = await service.get(`conversations/unreadCount`);
      setNotiCount(conversationData?.unreadCount);
      return conversationData;
    },
    refetchInterval: ONE_MINUTE,
  });

  useQuery({
    queryKey: ['courseNotification'],
    queryFn: async () => {
      const {
        data: { data: courseData },
      } = await service.get(`courses/summary`);
      setCourseNotificationCount(courseData?.unreadCount);
      return courseData;
    },
    refetchInterval: ONE_MINUTE,
  });
};

export default useNotificationCount;
