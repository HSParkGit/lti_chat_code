import service from '../api/apiService';
import { ApiError } from '../utils/errors';

export const chatServices = () => {
  const searchUsers = async (props) => {
    try {
      const response = await service.get('/chat/users/search', {
        params: props,
      });

      if (response?.errorCode) {
        throw new Error(response?.data?.errorMessage);
      }

      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch Calendar Service: ${error.message}`);
    }
  };

  const sendMessage = async (messageData) => {
    try {
      const response = await service.post('/chat/messages', messageData);
      if (response?.errorCode) {
        throw new Error(response?.data?.errorMessage);
      }
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch Calendar Service: ${error.message}`);
    }
  };

  const sendGroupMessage = async (messageData) => {
    try {
      // Convert groupId to chat_id format for backend
      const request = {
        chat_id: messageData.groupId,
        content: messageData.message,
        message_type: 'text',
      };
      const response = await service.post('/chat/messages', request);
      if (response?.errorCode) {
        throw new Error(response?.data?.errorMessage);
      }
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to send group message: ${error.message}`);
    }
  };

  const getChats = async () => {
    try {
      const response = await service.get('/chat/conversations');
      if (response?.errorCode) {
        throw new Error(response?.data?.errorMessage);
      }
      // Filter only direct/individual chats and add unread_count
      const allChats = response?.data || [];
      return allChats
        .filter(chat => chat.type === 'direct')
        .map(chat => ({
          ...chat,
          unread_count: chat.unread_count || 0,
        }));
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch chats: ${error.message}`);
    }
  };

  const getChat = async (chatId) => {
    try {
      const response = await service.get(`/chat/conversations/${chatId}/messages`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      // console.log(response.data, 'chat response');
      return response?.data?.content;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }

      throw new ApiError(`Failed to fetch Chat Service: ${error.message}`);
    }
  };

  const markAsRead = async (chat_id) => {
    try {
      const response = await service.post(`/chat/conversations/${chat_id}/read`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(`Failed to fetch Chat Service: ${error.message}`);
    }
  };

  const muteChat = async (user_id) => {
    try {
      const response = await service.post(`/chat/conversations/${user_id}/mute`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
    }
  };

  const unmuteChat = async (user_id) => {
    try {
      const response = await service.post(`/chat/conversations/${user_id}/unmute`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
    }
  };

  const deleteChat = async (user_id) => {
    try {
      const response = await service.delete(`/chat/conversations/${user_id}`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response.data.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }

      throw new ApiError(`Failed to fetch Chat Service: ${error.message}`);
    }
  };

  const createGroupChat = async (groupData) => {
    try {
      const response = await service.post('/chat/group', groupData);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }

      throw new ApiError(`Failed to create group chat: ${error.message}`);
    }
  };

  const getGroupChats = async () => {
    try {
      const response = await service.get('/chat/conversations');
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      // Filter only group chats and transform to expected format
      const allChats = response?.data || [];
      return allChats
        .filter(chat => chat.type === 'group')
        .map(chat => ({
          ...chat,
          groupId: chat.chat_id,
          groupName: chat.name,
          unread_count: chat.unread_count || 0,
        }));
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }

      throw new ApiError(`Failed to fetch group chats: ${error.message}`);
    }
  };

  const leaveGroup = async (groupId) => {
    try {
      // Use delete conversation endpoint to leave group
      const response = await service.delete(`/chat/conversations/${groupId}`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }

      throw new ApiError(`Failed to leave group: ${error.message}`);
    }
  };

  const getGroupChat = async (group_id) => {
    try {
      const response = await service.get(`/chat/conversations/${group_id}/messages`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response?.data?.content;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }

      throw new ApiError(`Failed to fetch group chat: ${error.message}`);
    }
  };

  const muteGroupChat = async (group_id) => {
    try {
      const response = await service.post(`/chat/conversations/${group_id}/mute`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
    }
  };

  const unmuteGroupChat = async (group_id) => {
    try {
      const response = await service.post(`/chat/conversations/${group_id}/unmute`);
      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
    }
  };

  const createDirectChat = async (studen_id) => {
    try {
      const response = await service.post(`chat/direct`, {
        other_user_id: studen_id,
      });

      if (response.errorCode) {
        throw new Error(response.data.errorMessage);
      }
      console.log(response);
      return response?.data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
    }
  };

  const getCurrentChat = () => {};

  return {
    searchUsers,
    sendMessage,
    getChats,
    getChat,
    createGroupChat,
    getGroupChats,
    deleteChat,
    markAsRead,
    muteChat,
    unmuteChat,
    leaveGroup,
    sendGroupMessage,
    getGroupChat,
    muteGroupChat,
    unmuteGroupChat,
    createDirectChat,
  };
};
