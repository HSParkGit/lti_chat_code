import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const createWebSocketService = () => {
  let stompClient = null;
  const groupSubscriptions = new Map();
  let connectionPromise = null;
  let activeUserId = null;
  let connectionStatus = 'disconnected'; // 'disconnected', 'connecting', 'connected', 'error'
  const statusHandlers = new Set();

  // Add status change listener
  const onStatusChange = (handler) => {
    statusHandlers.add(handler);
    // Return unsubscribe function
    return () => statusHandlers.delete(handler);
  };

  // Update status and notify all handlers
  const setStatus = (newStatus) => {
    if (connectionStatus !== newStatus) {
      connectionStatus = newStatus;
      statusHandlers.forEach((handler) => handler(newStatus));
    }
  };

  const connect = (userId, onIndividualMessage, groupIds, onGroupMessage) => {
    if (stompClient?.connected && userId === activeUserId) {
      return Promise.resolve(stompClient);
    }

    if (connectionPromise) {
      return connectionPromise;
    }

    const token = localStorage.getItem('accessToken'); // ⬅️ obtain JWT here

    setStatus('connecting');
    connectionPromise = new Promise((resolve, reject) => {
      if (stompClient) {
        disconnect();
      }

      stompClient = new Client({
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,

        webSocketFactory: () => new SockJS(`${import.meta.env.VITE_API_KEY?.replace('/api/v1', '')}/ws`),

        // ⬇️ IMPORTANT — send JWT during STOMP CONNECT
        connectHeaders: {
          Authorization: `Bearer ${token}`,
        },

        // debug: (str) => console.debug('STOMP:', str),
      });

      stompClient.onConnect = () => {
        activeUserId = userId;
        setStatus('connected');

        stompClient.subscribe(`/user/queue/messages`, (message) => {
          try {
            const parsedMessage = JSON.parse(message.body);
            console.log('Received WebSocket message:', parsedMessage);

            // Determine who the "other user" is
            const senderId = parsedMessage.sender_id === userId ? parsedMessage.receiver_id : parsedMessage.sender_id;

            onIndividualMessage(parsedMessage, senderId);
          } catch (error) {
            console.error('Error parsing private message:', error);
          }
        });

        updateGroupSubscriptions(groupIds, onGroupMessage);

        resolve(stompClient);
        connectionPromise = null;
      };

      stompClient.onStompError = (frame) => {
        console.error('STOMP protocol error:', frame);
        setStatus('error');
        reject(frame);
        connectionPromise = null;
      };

      stompClient.onWebSocketError = (error) => {
        console.error('WebSocket error:', error);
        setStatus('error');
        reject(error);
        connectionPromise = null;
      };

      stompClient.onDisconnect = () => {
        console.log('WebSocket disconnected');
        setStatus('disconnected');
        activeUserId = null;
      };

      stompClient.activate();
    });

    return connectionPromise;
  };

  const subscribeToGroup = async (groupId, onGroupMessage) => {
    if (!stompClient || !stompClient.connected) {
      console.warn('Cannot subscribe - WebSocket not connected');
      return;
    }
    const subscription = stompClient.subscribe(`/group/chat/${groupId}`, (message) => {
      try {
        const parsedMessage = JSON.parse(message.body);
        onGroupMessage(parsedMessage, groupId);
      } catch (error) {
        console.error(`Error parsing group message (${groupId}):`, error);
      }
    });

    groupSubscriptions.set(groupId, { subscription, callback: onGroupMessage });
  };

  const unsubscribeFromGroup = (groupId) => {
    try {
      if (groupSubscriptions.has(groupId)) {
        const { subscription } = groupSubscriptions.get(groupId);
        subscription.unsubscribe();
        groupSubscriptions.delete(groupId);
      }
    } catch (error) {
      console.log('error unsubscribing', error);
    }
  };

  const updateGroupSubscriptions = async (groupIds, onGroupMessage) => {
    if (!stompClient || !stompClient.connected || !groupIds) {
      console.warn('Cannot update subscriptions - WebSocket not connected or groupIds is undefined');
      return;
    }
    const currentGroups = new Set(groupSubscriptions.keys());
    const newGroups = new Set(groupIds);

    currentGroups.forEach((groupId) => {
      if (!newGroups.has(groupId)) {
        unsubscribeFromGroup(groupId);
      }
    });

    await Promise.all(
      groupIds.map((groupId) => {
        if (!currentGroups.has(groupId)) {
          return subscribeToGroup(groupId, onGroupMessage);
        }
        return Promise.resolve();
      })
    );
  };

  const disconnect = () => {
    if (stompClient) {
      groupSubscriptions.forEach((_, groupId) => {
        unsubscribeFromGroup(groupId);
      });

      stompClient.deactivate().then(() => {
        console.log('WebSocket successfully disconnected');
        stompClient = null;
        activeUserId = null;
      });
    }
  };

  return {
    connect,
    subscribeToGroup,
    unsubscribeFromGroup,
    updateGroupSubscriptions,
    disconnect,
    getStompClient: () => stompClient,
    getGroupSubscriptions: () => groupSubscriptions,
    isConnected: () => !!stompClient?.connected,
    onStatusChange,
    getStatus: () => connectionStatus,
  };
};
