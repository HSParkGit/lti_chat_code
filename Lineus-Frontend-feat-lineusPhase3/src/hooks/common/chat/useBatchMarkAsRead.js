import { useEffect, useRef } from 'react';
import { useMarkAsRead } from './ChatQueryHooks';
import { useQueryClient } from '@tanstack/react-query';
import { useChatParticipants } from './useChatParticipants';
import useChat from './useChat';
import { useUserStore } from '@/store/useUserStore';

export function useBatchMarkAsRead(isIndividual, isGroup) {
  console.log(isIndividual, isGroup, 'batch mark as read hook');
  const queueRef = useRef(new Set());
  const timerRef = useRef(null);
  const queryClient = useQueryClient();
  const markAsReadMutation = useMarkAsRead();
  const { currentChat } = useChat();
  const { userId } = useUserStore((state) => state.getUserId());
  const { otherUserId, groupId } = useChatParticipants(currentChat, userId, isIndividual, isGroup);

  const flushQueue = async () => {
    const ids = Array.from(queueRef.current);
    queueRef.current.clear();
    timerRef.current = null;

    if (ids.length === 0) return;

    try {
      await markAsReadMutation.mutateAsync(ids);

      if (isIndividual) {
        queryClient.invalidateQueries({ queryKey: ['messages', otherUserId] });
      }
      if (isGroup) {
        queryClient.invalidateQueries({ queryKey: ['group-messages', groupId] });
      }

      const invalidateKeys = isIndividual ? ['individualChats'] : ['groupChats'];

      await Promise.all(invalidateKeys.map((key) => queryClient.invalidateQueries({ queryKey: [key] })));
    } catch (error) {
      console.error('Failed to batch mark as read:', error);
    }
  };

  const addToQueue = (messageId) => {
    queueRef.current.add(messageId);

    if (!timerRef.current) {
      timerRef.current = setTimeout(flushQueue, 500);
    }
  };

  useEffect(() => {
    return () => {
      if (timerRef.current) {
        clearTimeout(timerRef.current);
      }
    };
  }, []);

  return addToQueue;
}
