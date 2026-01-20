import { useMemo } from 'react';

export const useChatParticipants = (currentChat, userId, isIndividual, isGroup) => {
  // console.log(currentChat, userId, isIndividual, isGroup, 'chat participants hook');
  const otherUserId = useMemo(() => {
    if (!isIndividual || !currentChat) return null;
    return currentChat.senderId === userId ? currentChat.receiverId : currentChat.senderId;
  }, [currentChat, userId, isIndividual]);

  const groupId = useMemo(() => {
    if (!isGroup || !currentChat) return null;
    return currentChat.groupId;
  }, [currentChat, isGroup]);

  return { otherUserId, groupId };
};
