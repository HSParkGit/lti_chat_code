import { useMemo } from 'react';
import useChat from './useChat';
import { useUserStore } from '../../../store/useUserStore';
import { useChatType } from '../../../view/page/dashboard/chat/providers/useChatTypeProvider';

export const useCommonCurrentChat = () => {
  const { currentChat } = useChat();
  const userId = useUserStore((state) => state.getUserId());
  const user_id = userId && Number(userId);
  const { isIndividual } = useChatType();
  const user = useMemo(() => {
    if (!currentChat) return null;
    return isIndividual
      ? currentChat.participants.filter((participant) => participant.id !== user_id)[0]
      : { id: currentChat.groupId, name: currentChat.groupName, profile: '' };
  }, [isIndividual, currentChat, userId]);

  return { user };
};

// return isIndividual
// ? chat.participants.filter((participant) => participant.id !== userId)[0]
// : { id: chat.groupId, name: chat.groupName, senderId: chat.senderId, senderName: chat.senderName };
