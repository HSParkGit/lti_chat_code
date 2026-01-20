import { Input } from '@headlessui/react';
import { FiEdit, FiSearch } from 'react-icons/fi';
import React, { useEffect, useMemo, useCallback, useState } from 'react';
import EmptyConversationList from './EmptyConversationList';
import EmptyGroupChat from './EmptyGroupChat';
import ConversationTap from './ConversationTap';
import { useChatType } from './providers/useChatTypeProvider';
import CreateChatDialog from './Dialogs/CreateChat';
import { useUserStore } from '../../../../store/useUserStore';
import { useIndividualChatsStore } from '../../../../store/useIndividualChatStore';
import MessageContainer from './MessageContainer';
import { useGroupChatsStore } from '../../../../store/useGroupChatStore';
import useChat from '../../../../hooks/common/chat/useChat';
import {
  useConversationsWithGroupMessages,
  useConversationsWithMessages,
  useFetchGroupChats,
} from '../../../../hooks/common/chat/ChatQueryHooks';
import { useCommonCurrentChat } from '../../../../hooks/common/chat/useCommonCurrentChat';
import { useTranslation } from 'react-i18next';
import { useDebounce } from '../../../../hooks/common/useDebounce';

const MemoizedMessageContainer = React.memo(MessageContainer);

export default function ChatInterface({ messageType }) {
  // component states
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);

  // contexts and util hooks
  const { isIndividual, isGroup } = useChatType();
  const debouncedSearchTerm = useDebounce(searchTerm, 300);
  const { user } = useCommonCurrentChat();
  const { t } = useTranslation();
  const userId = useUserStore((state) => state.getUserId());

  // Individual chat snapshot
  const stableIndividualList = useIndividualChatsStore((state) => state.stableUnreadList);
  const createIndividualSnapshot = useIndividualChatsStore((state) => state.createUnreadSnapshot);
  const clearIndividualSnapshot = useIndividualChatsStore((state) => state.clearUnreadSnapshot);

  // Group chat snapshot
  const stableGroupList = useGroupChatsStore((state) => state.stableUnreadList);
  const createGroupSnapshot = useGroupChatsStore((state) => state.createUnreadSnapshot);
  const clearGroupSnapshot = useGroupChatsStore((state) => state.clearUnreadSnapshot);

  // other store hooks
  const { chats, currentChat, messageInput, clearMessageInput, setMessageInput, setCurrentChat, unread, groupUnread } =
    useChat();
  const { initUnreadCounts } = useIndividualChatsStore((state) => ({ initUnreadCounts: state.initUnreadCounts }));
  const { initUnreadCountsGroup } = useGroupChatsStore((state) => ({
    initUnreadCountsGroup: state.initUnreadCountsGroup,
  }));

  // query and mutation hooks
  const { data: conversations } = useConversationsWithMessages(userId, currentChat?.chat_id);
  const { data: groupConversations } = useConversationsWithGroupMessages(userId);
  const { data: allGroupChats = [] } = useFetchGroupChats(false);

  // memoized values
  const unreadChats = useMemo(() => (isIndividual ? unread : groupUnread), [unread, groupUnread, isIndividual]);

  // data initialization effects
  useEffect(() => {
    if (conversations) initUnreadCounts(conversations, userId);
  }, [conversations, userId, initUnreadCounts]);

  useEffect(() => {
    if (groupConversations) initUnreadCountsGroup(groupConversations);
  }, [groupConversations, initUnreadCountsGroup]);

  useEffect(() => {
    if (messageType === 'unread') {
      if (isIndividual) createIndividualSnapshot();
      else if (isGroup) createGroupSnapshot();
    }
    return () => {
      if (isIndividual) clearIndividualSnapshot();
      else if (isGroup) clearGroupSnapshot();
    };
  }, [
    messageType,
    isIndividual,
    isGroup,
    createIndividualSnapshot,
    clearIndividualSnapshot,
    createGroupSnapshot,
    clearGroupSnapshot,
  ]);

  // handlers
  const handleSendSuccess = useCallback((chatId) => clearMessageInput(chatId), [clearMessageInput]);
  const handleInputChange = useCallback((chatId, value) => setMessageInput(chatId, value), [setMessageInput]);

  const handleConversationSearch = useCallback(
    (term) => {
      let sourceList = chats;

      if (term.trim() === '') {
        setSearchResults([]);
        setIsSearching(false);
        return;
      }
      setIsSearching(true);
      const results = sourceList.filter((chat) => {
        const nameToCheck = isIndividual
          ? chat.participants
              .filter((participant) => participant.id !== Number(userId))
              .map((participant) => participant.name)
              .join(' ')
          : chat.groupName;
        return nameToCheck.toLowerCase().includes(term.toLowerCase());
      });
      setSearchResults(results);
    },
    [chats, messageType, isIndividual, userId, stableIndividualList, stableGroupList]
  );

  useEffect(() => {
    handleConversationSearch(debouncedSearchTerm);
  }, [debouncedSearchTerm, handleConversationSearch]);

  const chatsToDisplay = useMemo(() => {
    if (isSearching) return searchResults;
    if (messageType === 'all') return chats;
    if (messageType === 'unread') {
      return chats.filter((chat) => chat.unread_count > 0);
    }
    return [];
  }, [isSearching, searchResults, messageType, chats, isIndividual, stableIndividualList, stableGroupList]);

  const shouldShowCurrentChat = () => {
    if (isGroup) return currentChat && allGroupChats.some((chat) => chat?.groupId === currentChat?.groupId);
    return currentChat;
  };

  return (
    <div className='flex h-full min-h-0 w-full flex-1 pb-4'>
      <article className='border-1 flex h-full min-h-0 w-full flex-1 basis-2/5 flex-col overflow-auto border border-l-0 border-stroke100 py-4'>
        <div className='my-2 mr-4 flex flex-grow-0 items-center justify-between'>
          <h4 className='font-bold'>{t('chat-room.all-messages')}</h4>
          {isIndividual && messageType === 'all' && (
            <CreateChatDialog>
              <FiEdit />
            </CreateChatDialog>
          )}
        </div>
        {chats && chats.length > 0 ? (
          <div className='flex flex-col gap-2 overflow-auto'>
            <div className='relative mr-3 mt-2'>
              <FiSearch className='absolute left-3 top-1/2 -translate-y-1/2 transform text-gray-600' />
              <Input
                className='border-1 h-8 w-full rounded-full border px-1 py-2 indent-8 placeholder:text-sm focus:border-green-400 focus:outline-none'
                placeholder={t('chat-room.search')}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
            <div className='mt-4 h-full w-full overflow-y-scroll'>
              {chatsToDisplay.length > 0 ? (
                chatsToDisplay.map((chat) => (
                  <ConversationTap
                    unread={chat.unread_count}
                    key={isIndividual ? chat.id : chat.groupId}
                    chat={chat}
                    setCurrentChat={setCurrentChat}
                    currentChat={currentChat}
                    currentChatUser={user}
                  />
                ))
              ) : isSearching ? (
                <div className='p-4 text-center text-gray-500'>{t('chat-room.no-conversation-found')}</div>
              ) : null}
            </div>
          </div>
        ) : (
          <EmptyConversationList />
        )}
      </article>
      <article className='border-1 flex h-full min-h-0 w-full flex-1 basis-3/5 border border-l-0 border-stroke100'>
        {shouldShowCurrentChat() ? (
          <MemoizedMessageContainer
            currentChat={currentChat}
            chats={chats}
            user={user}
            messageInput={messageInput[user?.id] || ''}
            onInputChange={handleInputChange}
            onSendSuccess={handleSendSuccess}
          />
        ) : (
          <EmptyGroupChat key='emptyGroupChat' />
        )}
      </article>
    </div>
  );
}
