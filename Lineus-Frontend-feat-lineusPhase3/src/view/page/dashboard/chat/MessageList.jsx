import { useEffect, useRef, useCallback, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import Message from './Message';
import { useUserStore } from '../../../../store/useUserStore';
import { useChatType } from './providers/useChatTypeProvider';
import { formatMessageGroupLabel } from '../../../../libs/utils/chat/date';
import { useFetchGroupChat, useFetchIndividualChat } from '@/hooks/common/chat/ChatQueryHooks';
import { useMessages } from './providers/useMessages';
import { useCommonCurrentChat } from '@/hooks/common/chat/useCommonCurrentChat';
import { useBatchMarkAsRead } from '@/hooks/common/chat/useBatchMarkAsRead';
import useChat from '@/hooks/common/chat/useChat';

export default function MessageList() {
  const { t, i18n } = useTranslation();
  const messagesContainerRef = useRef(null);
  const messageRefs = useRef({});
  const processedMessages = useRef(new Set());
  const userId = useUserStore((state) => state.getUserId());
  const user_id = userId && Number(userId);
  const { chatType, isIndividual, isGroup } = useChatType();
  const { user } = useCommonCurrentChat();
  const { currentChat } = useChat();
  const { data: allIndividualMessages } = useFetchIndividualChat(currentChat && currentChat.chat_id);
  // const { data: allGroupMessages } = useFetchGroupChat(user && isGroup && user.id);
  const { messages, setMessages } = useMessages();
  const skipScrollRef = useRef(false);
  const addToQueue = useBatchMarkAsRead(isIndividual, isGroup);
  const allMessages = useMemo(() => {
    if (allIndividualMessages === undefined) {
      return [];
    } else {
      if (isIndividual) {
        console.log('Fetched individual messages:', allIndividualMessages);
        return allIndividualMessages || [];
      } else {
        // return allGroupMessages.content || [];
      }
    }
  }, [isIndividual, allIndividualMessages]);

  console.log(allMessages, 'all messages memoized');

  useEffect(() => {
    if (allMessages) {
      console.log('Setting messages from fetch:', allMessages);
      setMessages(allMessages);
    }
  }, [allMessages, setMessages]);

  useEffect(() => {
    const container = messagesContainerRef.current;
    if (!container) return;

    if (skipScrollRef.current) {
      skipScrollRef.current = false;
      return;
    }

    const raf = requestAnimationFrame(() => {
      container.scrollTop = container.scrollHeight;
    });

    return () => cancelAnimationFrame(raf);
  }, [messages]);

  // Mark messages as read when visible using batch hook
  useEffect(() => {
    const container = messagesContainerRef.current;
    if (!container || !messages || messages.length === 0) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            console.log(entry.target.dataset, 'observing message visibility');
            const messageId = entry.target.dataset.messageId;
            if (!messageId) return;
            const isRead = entry.target.dataset.read === 'true';
            console.log(isRead, 'isRead for message', messageId);
            console.log(chatType, 'chatType for message', messageId);
            console.log(processedMessages.current.has(messageId), 'processedMessages for message', messageId);
            const self = chatType === 'Group' ? false : entry.target.dataset.sender_id === user_id;
            if (!isRead && !processedMessages.current.has(messageId) && !self) {
              addToQueue(messageId);
              processedMessages.current.add(messageId);
            }
          }
        });
      },
      { root: container, rootMargin: '0px', threshold: 0.1 }
    );

    const currentRefs = messageRefs.current;
    Object.values(currentRefs).forEach((el) => {
      if (el) observer.observe(el);
    });

    return () => observer.disconnect();
  }, [messages, userId, chatType]);

  const setMessageRef = useCallback((node, id) => {
    if (node) {
      messageRefs.current[id] = node;
    } else {
      delete messageRefs.current[id];
    }
  }, []);

  function groupMessagesByDate(messages) {
    console.log(messages, 'grouping messages by date');
    if (!messages) return {};
    const groups = {};
    messages.forEach((message) => {
      const date = new Date(message.timestamp);
      const dateKey = date.toDateString();
      if (!groups[dateKey]) {
        groups[dateKey] = [];
      }
      groups[dateKey].push(message);
    });
    return groups;
  }

  const groupedMessages = useMemo(() => groupMessagesByDate(messages), [messages]);

  console.log(groupedMessages, 'grouped messages');

  return (
    <div className='h-full min-h-0 flex-1 overflow-y-auto p-6' ref={messagesContainerRef}>
      {Object.entries(groupedMessages).map(([dateKey, msgs]) => {
        const dateLabel = t && msgs[0]?.sentAt ? formatMessageGroupLabel(msgs[0].sentAt, { t, i18n }) : '';
        const shouldShowDivider =
          dateLabel === t('chat-title.groupLabel.today') || dateLabel === t('chat-title.groupLabel.yesterday');

        return (
          <div key={dateKey} className='min-h-0 space-y-2'>
            <div className='flex items-center justify-center gap-4 space-y-2'>
              {shouldShowDivider && <div className='h-px flex-1 bg-slate-200' />}
              <span className='whitespace-nowrap text-sm font-medium text-gray-500'>
                {formatMessageGroupLabel(msgs[0].timestamp)}
              </span>
              {shouldShowDivider && <div className='h-px flex-1 bg-slate-200' />}
            </div>
            {msgs.map((message) => (
              <div
                key={message.id}
                ref={(node) => setMessageRef(node, message.id)}
                data-message-id={message.id}
                data-read={message.read}
                data-sender-id={message.sender_id}
                className='space-y-2'
              >
                <Message message={message} />
              </div>
            ))}
          </div>
        );
      })}
    </div>
  );
}
