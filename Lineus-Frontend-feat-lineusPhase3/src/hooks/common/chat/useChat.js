import { useMemo } from 'react';
import { useGroupChatsStore } from '../../../store/useGroupChatStore';
import { useIndividualChatsStore } from '../../../store/useIndividualChatStore';
import { useChatType } from '../../../view/page/dashboard/chat/providers/useChatTypeProvider';

export default function useChat() {
  const { isIndividual } = useChatType();

  // Call all hooks unconditionally first
  const individualChats = useIndividualChatsStore((state) => state.chats);
  const groupChats = useGroupChatsStore((state) => state.chats);
  const individualCurrentChat = useIndividualChatsStore((state) => state.currentChat);
  const groupCurrentChat = useGroupChatsStore((state) => state.currentChat);
  const individualUnreadChats = useIndividualChatsStore((state) => state.getUnreadChats);
  const groupUnreadChats = useGroupChatsStore((state) => state.getUnreadChats);
  const setIndividualCurrentChat = useIndividualChatsStore((state) => state.setCurrentChat);
  const setGroupCurrentChat = useGroupChatsStore((state) => state.setCurrentChat);
  const setIndividualChats = useIndividualChatsStore((state) => state.setChats);
  const setGroupChats = useGroupChatsStore((state) => state.setChats);
  const setIndividualMessages = useIndividualChatsStore((state) => state.setCurrentChatMessages);
  const individualMessages = useIndividualChatsStore((state) => state.currentChatMessages);
  const addNewIndividualChat = useIndividualChatsStore((state) => state.addNewChat);
  const addIndividualMessage = useIndividualChatsStore((state) => state.addMessage);
  const unreadCounts = useIndividualChatsStore((state) => state.unreadCounts);
  const unreadCountsGroup = useGroupChatsStore((state) => state.unreadCountsGroup);
  const individualMessageInput = useIndividualChatsStore((state) => state.messageInput);
  const setIndividualMessageInput = useIndividualChatsStore((state) => state.setMessageInput);
  const clearIndividualMessageInput = useIndividualChatsStore((state) => state.clearMessageInput);
  const groupMessageInput = useGroupChatsStore((state) => state.messageInput);
  const setGroupMessageInput = useGroupChatsStore((state) => state.setMessageInput);
  const clearGroupMessageInput = useGroupChatsStore((state) => state.clearMessageInput);

  // Then select values based on chat type
  const chats = isIndividual ? individualChats : groupChats;
  const currentChat = isIndividual ? individualCurrentChat : groupCurrentChat;
  const getUnreadChats = isIndividual ? individualUnreadChats : groupUnreadChats;
  const setCurrentChat = isIndividual ? setIndividualCurrentChat : setGroupCurrentChat;
  const setCurrentChatMessages = isIndividual ? setIndividualMessages : () => {};
  const currentMessages = isIndividual ? individualMessages : [];
  const unread = unreadCounts;
  const groupUnread = unreadCountsGroup;
  const messageInput = isIndividual ? individualMessageInput : groupMessageInput;
  const setMessageInput = isIndividual ? setIndividualMessageInput : setGroupMessageInput;
  const clearMessageInput = isIndividual ? clearIndividualMessageInput : clearGroupMessageInput;

  const chatData = useMemo(
    () => ({
      chats,
      currentChat,
      groupCurrentChat,
      individualCurrentChat,
      getUnreadChats,
      setCurrentChat,
      setGroupCurrentChat,
      setIndividualCurrentChat,
      addNewChat: addNewIndividualChat,
      setCurrentChatMessages,
      currentMessages,
      setChats: setIndividualChats,
      setGroupChats,
      addMessage: addIndividualMessage,
      unread,
      groupUnread,
      messageInput,
      setMessageInput,
      clearMessageInput,
    }),
    [
      chats,
      currentChat,
      groupCurrentChat,
      individualCurrentChat,
      getUnreadChats,
      setCurrentChat,
      setGroupCurrentChat,
      setIndividualCurrentChat,
      addNewIndividualChat,
      setCurrentChatMessages,
      currentMessages,
      setIndividualChats,
      setGroupChats,
      addIndividualMessage,
      unread,
      groupUnread,
      isIndividual,
      setMessageInput,
      clearMessageInput,
      messageInput,
    ]
  );
  return chatData;
}
