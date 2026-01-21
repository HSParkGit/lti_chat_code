import { useMutation, useQueries, useQuery } from '@tanstack/react-query';
import { chatServices } from '../../../services/chatServices';
import { useCallback } from 'react';
import { useIndividualChatsStore } from '../../../store/useIndividualChatStore';
import { useMemo } from 'react';

export const useDeleteChat = () => {
  return useMutation({
    mutationKey: ['deleteChat'],
    mutationFn: (user_id) => chatServices().deleteChat(user_id),
  });
};

export const useCreateGroupChat = () => {
  return useMutation({
    mutationKey: ['createGroupChat'],
    mutationFn: (groupChatData) => chatServices().createGroupChat(groupChatData),
  });
};

export const useConversationsWithMessages = (currentUserId, chatId) => {
  const { data: conversations, isLoading: isConversationsLoading } = useQuery({
    queryKey: ['conversations', currentUserId],
    queryFn: () => chatServices().getChats(),
    enabled: !!chatId,
    staleTime: 1000 * 60 * 5, // 5 minutes
  });

  const messageQueries = useQueries({
    queries: conversations
      ? conversations.map((conv) => {
          return {
            queryKey: ['messages', chatId],
            queryFn: () => chatServices().getChat(chatId),
            staleTime: 1000 * 60 * 5,
            enabled: !!conversations, // Only run when conversations have been fetched
          };
        })
      : [], // If conversations aren't loaded, pass an empty array
  });

  const data = useMemo(() => {
    if (!conversations) return [];

    return conversations.map((conv, index) => {
      const messagesResult = messageQueries[index];
      const messages = messagesResult?.data?.content || [];
      // Calculate the unread count from the fetched messages
      const unreadCount = messages.filter((m) => !m.read && m.sender_id !== currentUserId).length;

      return {
        ...conv,
        unreadCount,
        isMessagesLoading: messagesResult?.isLoading || false,
      };
    });
  }, [conversations, messageQueries, currentUserId]);

  return { data, isLoading: isConversationsLoading };
};

export const useFetchIndividualChat = (chatId) => {
  return useQuery({
    queryKey: ['messages', chatId],  // Same key as useConversationsWithMessages for deduplication
    queryFn: async () => {
      console.log('Fetching individual chat for chatId:', chatId);
      const response = await chatServices().getChat(chatId);
      return (response || []).slice().reverse();
    },
    enabled: !!chatId,
    keepPreviousData: true,
    staleTime: 1000 * 60, // Cache for 1 minute to prevent duplicate fetches
    refetchOnWindowFocus: false,
  });
};

export const useFetchIndividualChats = (select) => {
  const chats = useIndividualChatsStore((state) => state.chats);

  return useQuery({
    queryKey: ['individualChatsFetch'],
    queryFn: async () => {
      const response = await chatServices().getChats();
      return response;
    },
    select: useCallback(
      (data) => {
        const localOnlyChats = chats.filter(
          (localChat) =>
            localChat.isLocalOnly &&
            !data.some(
              (apiChat) => apiChat.receiverId === localChat.receiverId || apiChat.senderId === localChat.receiverId
            )
        );
        const combinedChats = [...localOnlyChats, ...data].sort((a, b) => new Date(b.sentAt) - new Date(a.sentAt));

        return select ? select(combinedChats) : combinedChats;
      },
      [select, chats]
    ),
    keepPreviousData: true,
    staleTime: 0,
    refetchOnWindowFocus: false,
  });
};

export const useFetchGroupChat = (groupId) => {
  return useQuery({
    queryKey: ['groupChat', groupId],
    queryFn: async () => {
      const response = await chatServices().getGroupChat(groupId);
      return (response || []).slice().reverse();
    },
    keepPreviousData: true,
    enabled: !!groupId,
    staleTime: 0,
    // refetchOnWindowFocus: false,
  });
};

export const useFetchGroupChats = (select) => {
  return useQuery({
    queryKey: ['groupChats'],
    queryFn: async () => {
      const response = await chatServices().getGroupChats();
      const chats = response || [];

      const sorted = chats.sort((a, b) => {
        const dateA = a.sentAt ? new Date(a.sentAt).getTime() : 0;
        const dateB = b.sentAt ? new Date(b.sentAt).getTime() : 0;
        return dateB - dateA;
      });

      return sorted;
    },
    select: useCallback(
      (data) => {
        return select ? select(data) : data;
      },
      [select]
    ),
    keepPreviousData: true,
    staleTime: 1000 * 60 * 5,
    refetchOnWindowFocus: false,
  });
};

export const useMarkAsRead = () => {
  return useMutation({
    mutationKey: ['markAsRead'],
    mutationFn: (id) => chatServices().markAsRead(id),
  });
};

export const useMuteChat = () => {
  return useMutation({
    mutationKey: ['muteChat'],
    mutationFn: (id) => chatServices().muteChat(id),
  });
};

export const useSearchUsers = (params, userId) => {
  return useQuery({
    queryKey: ['users', params],
    queryFn: async () => {
      const users = await chatServices().searchUsers(params);
      return users.filter((u) => u.id !== userId);
    },
    enabled: !!params,
  });
};

export function useSendMessage() {
  return useMutation({
    mutationKey: ['sendMessage'],
    mutationFn: (messageData) => chatServices().sendMessage(messageData),
  });
}

export function useSendGroupMessage() {
  return useMutation({
    mutationKey: ['sendGroupMessage'],
    mutationFn: (messageData) => chatServices().sendGroupMessage(messageData),
  });
}

export const useUnmuteChat = () => {
  return useMutation({
    mutationKey: ['unmuteChat'],
    mutationFn: (id) => chatServices().unmuteChat(id),
  });
};

export const useConversationsWithGroupMessages = (userId) => {
  const { data: conversations, isLoading: isConversationsLoading } = useQuery({
    queryKey: ['group-conversations', userId],
    queryFn: () => chatServices().getGroupChats(),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5, // Cache for 5 minutes.
  });

  const messageQueries = useQueries({
    queries: conversations
      ? conversations.map((conv) => {
          return {
            queryKey: ['group-messages', conv.groupId],
            queryFn: () => chatServices().getGroupChat(conv.groupId),
            staleTime: 1000 * 60 * 5,
            enabled: !!conversations,
          };
        })
      : [], // Pass an empty array to `useQueries` while the initial fetch is loading.
  });

  const data = useMemo(() => {
    if (!conversations) {
      return [];
    }

    return conversations.map((conv, index) => {
      const messagesResult = messageQueries[index];
      const messages = messagesResult.data || [];

      const unreadCount = messages.filter((m) => !m.read && m.senderId !== userId).length;

      return {
        ...conv,
        unreadCount,
        isMessagesLoading: messagesResult.isLoading,
      };
    });
  }, [conversations, messageQueries, userId]);

  return { data, isLoading: isConversationsLoading };
};

export const useMuteGroup = () => {
  return useMutation({
    mutationKey: ['muteGroupChat'],
    mutationFn: (id) => chatServices().muteGroupChat(id),
  });
};

export const useUnmuteGroupChat = () => {
  return useMutation({
    mutationKey: ['unmuteGroupChat'],
    mutationFn: (id) => chatServices().unmuteGroupChat(id),
  });
};

export const useCreateDirectChat = () => {
  return useMutation({
    mutationKey: ['createDirectChat'],
    mutationFn: (id) => chatServices().createDirectChat(id),
  });
};
