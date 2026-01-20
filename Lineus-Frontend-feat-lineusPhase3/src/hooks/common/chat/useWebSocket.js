import { useEffect, useRef, useCallback, useState } from 'react';
import { createWebSocketService } from '../../../view/page/dashboard/chat/service/webSocketService';
import { isEqual } from 'lodash';

export const useWebSocket = (userId, groupIds, handleIndividualMessage, handleGroupMessage) => {
  const [connectionStatus, setConnectionStatus] = useState('disconnected');

  const wsService = useRef(createWebSocketService()).current;
  const prevGroupIds = useRef([]);
  const maxRetries = 5;
  const retryDelay = 1000;
  const connectWithRetry = useCallback(
    async (retryCount = 0) => {
      try {
        await wsService.connect(userId, handleIndividualMessage, groupIds, handleGroupMessage);
      } catch (error) {
        if (retryCount < maxRetries) {
          setTimeout(() => {
            connectWithRetry(retryCount + 1);
          }, retryDelay * retryCount);
        } else {
          console.error('Max retries reached. Unable to connect to WebSocket.');
        }
      }
    },
    [userId, handleIndividualMessage, handleGroupMessage]
  );

  useEffect(() => {
    const unsubscribe = wsService.onStatusChange((status) => {
      setConnectionStatus(status);
    });
    return () => {
      unsubscribe();
    };
  }, []);

  // const connectWithRetry = useCallback(async () => {
  //   try {
  //     await wsService.connect(
  //       userId,
  //       messageHandlers.handleIndividualMessage,
  //       groupIds,
  //       messageHandlers.handleGroupMessage
  //     );
  //   } catch (error) {
  //     setTimeout(connectWithRetry, Math.min(5000));
  //   }
  // }, [userId, messageHandlers]);

  // Connection effect
  useEffect(() => {
    if (!userId) return;
    if (connectionStatus === 'disconnected') {
      connectWithRetry();
    }
  }, [userId, connectWithRetry, connectionStatus]);

  // Group subscription effect
  useEffect(() => {
    if (!groupIds || connectionStatus !== 'connected') return;

    // Deep compare to prevent unnecessary updates
    if (isEqual(groupIds, prevGroupIds.current)) {
      return;
    }
    prevGroupIds.current = groupIds;
    wsService.updateGroupSubscriptions(groupIds, handleGroupMessage);
  }, [groupIds, handleGroupMessage, connectionStatus]);

  return wsService;
};
