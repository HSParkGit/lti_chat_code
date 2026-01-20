import { create } from 'zustand';
import { useUserStore } from './useUserStore';
import isEqual from 'lodash.isequal';

export const individualChatsStore = create((set, get) => ({
  chats: [],
  currentChat: null,
  messageInput: {},
  setMessageInput: (chatId, value) => set((state) => ({ messageInput: { ...state.messageInput, [chatId]: value } })),
  clearMessageInput: (chatId) => set((state) => ({ messageInput: { ...state.messageInput, [chatId]: '' } })),
  currentOtherUserId: null,
  currentChatMessages: [],
  setCurrentChat: (currentChat) => {
    const userId = useUserStore.getState().getUserId();
    set({ currentChat });
    if (currentChat && userId) {
      set({ currentOtherUserId: currentChat.senderId === userId ? currentChat.receiverId : currentChat.senderId });
      return;
    }
    set({ currentOtherUserId: null });
  },
  setChats: (chats) => set({ chats }),
  addNewChat: (chat) => set((state) => ({ chats: [chat, ...state.chats] })),
  removeNewChat: (chatId) => set((state) => ({ chats: state.chats.filter((chat) => chat.receiverId !== chatId) })),
  setCurrentChatMessages: async (messages) => set({ currentChatMessages: messages }),

  getUnreadChats: (userId) => {
    return get().chats.filter((chat) => {
      const unreadCountInfo = get().unreadCounts[chat.receiverId === userId ? chat.senderId : chat.receiverId];
      return (chat.senderId !== userId && !chat.read) || (unreadCountInfo && unreadCountInfo.unreadCount > 0);
    });
  },

  // Unread counts
  unreadCounts: {},
  initUnreadCounts: (conversations, currentUserId) => {
    const newUnreadCounts = conversations.reduce((acc, conv) => {
      const otherUserId = conv.senderId === currentUserId ? conv.receiverId : conv.senderId;
      acc[otherUserId] = { unreadCount: conv.unreadCount || 0, userId: otherUserId };
      return acc;
    }, {});

    const currentUnreadCounts = get().unreadCounts;

    if (!isEqual(currentUnreadCounts, newUnreadCounts)) {
      set({ unreadCounts: newUnreadCounts });
    }
  },
  incrementUnread: (conversationId) => {
    set((state) => ({
      unreadCounts: {
        ...state.unreadCounts,
        [conversationId]: {
          ...state.unreadCounts[conversationId],
          unreadCount: (state.unreadCounts[conversationId]?.unreadCount || 0) + 1,
        },
      },
    }));
  },

  resetUnread: (chatId) =>
    set((state) => ({
      chats: state.chats.map((c) => (c.chat_id === chatId ? { ...c, unread_count: 0 } : c)),
    })),

  resetUnMute: (chatId) =>
    set((state) => ({
      chats: state.chats.map((c) => (c.chat_id === chatId ? { ...c, muted: false } : c)),
      currentChat:
        state.currentChat && state.currentChat.chat_id === chatId
          ? { ...state.currentChat, muted: false }
          : state.currentChat,
    })),

  resetMute: (chatId) =>
    set((state) => ({
      chats: state.chats.map((c) => (c.chat_id === chatId ? { ...c, muted: true } : c)),
      currentChat:
        state.currentChat && state.currentChat.chat_id === chatId
          ? { ...state.currentChat, muted: true }
          : state.currentChat,
    })),

  deleteChatFromStore: (chatId) =>
    set((state) => ({
      chats: state.chats.filter((c) => c.chat_id !== chatId),
    })),

  stableUnreadList: [],
  createUnreadSnapshot: () => {
    const userId = useUserStore.getState().getUserId();
    if (!userId) return;
    const unread = get().getUnreadChats(userId);
    set({ stableUnreadList: unread });
  },
  clearUnreadSnapshot: () => set({ stableUnreadList: [] }),
}));

export const useIndividualChatsStore = individualChatsStore;
