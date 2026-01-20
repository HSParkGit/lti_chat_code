import { create } from 'zustand';
import isEqual from 'lodash.isequal';
import { useUserStore } from './useUserStore';

export const groupChatsStore = create((set, get) => ({
  chats: [],
  currentChat: null,
  messageInput: {},
  conversations: [],
  currentGroupId: null,
  setMessageInput: (groupId, value) => set((state) => ({ messageInput: { ...state.messageInput, [groupId]: value } })),
  clearMessageInput: (groupId) => set((state) => ({ messageInput: { ...state.messageInput, [groupId]: '' } })),
  setChats: (chats) => {
    set({ chats: chats });
  },
  setConversations: (conversations) => {
    set({ conversations: conversations });
  },
  updateOneConversation: (conversation) => {
    set((state) => ({
      conversations: state.conversations.map((conv) =>
        conv.groupId === conversation.groupId ? { ...conv, ...conversation } : conv
      ),
    }));
  },
  setCurrentChat: (currentChat) => {
    if (currentChat) {
      set({ currentGroupId: currentChat?.groupId });
    } else {
      set({ currentGroupId: null });
    }
    set({ currentChat: currentChat });
  },
  getUnreadChats: (userId) => {
    return get().chats.filter(
      (chat) =>
        !!chat.sentAt &&
        ((chat.senderId !== userId && !chat.read) ||
          (get().unreadCountsGroup[chat.groupId] && get().unreadCountsGroup[chat.groupId]?.unreadCount > 0))
    );
  },
  unreadCountsGroup: {},
  initUnreadCountsGroup: (conversations) => {
    const newUnreadCountsGroup = conversations.reduce((acc, conv) => {
      acc[conv.groupId] = { unreadCount: conv.unreadCount || 0, userId: conv.groupId };
      return acc;
    }, {});
    const currentUnreadCountsGroup = get().unreadCountsGroup;
    if (!isEqual(currentUnreadCountsGroup, newUnreadCountsGroup)) {
      set({ unreadCountsGroup: newUnreadCountsGroup });
    }
  },
  incrementUnread: (groupId) => {
    set((state) => ({
      unreadCountsGroup: {
        ...state.unreadCountsGroup,
        [groupId]: {
          ...state.unreadCountsGroup[groupId],
          unreadCount: (state.unreadCountsGroup[groupId]?.unreadCount || 0) + 1,
        },
      },
    }));
  },

  stableUnreadList: [],
  createUnreadSnapshot: () => {
    const userId = useUserStore.getState().getUserId();
    if (!userId) return;
    const unread = get().getUnreadChats(userId);
    set({ stableUnreadList: unread });
  },
  clearUnreadSnapshot: () => set({ stableUnreadList: [] }),
}));

export const useGroupChatsStore = groupChatsStore;
