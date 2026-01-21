import { Tab, TabGroup, TabList, TabPanel, TabPanels } from '@headlessui/react';
import { twMerge } from 'tailwind-merge';
import { ChatIcon, GroupIcon } from '../../../../components/common/Icon';
import React, { useEffect, useMemo, useState, useCallback, memo } from 'react';
import { ChatTypeProvider, useChatType } from './providers/useChatTypeProvider';
import ChatSection from './ChatSection';
import { MessageProvider, useMessages } from './providers/useMessages';
import { useUserStore } from '../../../../store/useUserStore';
import { useQueryClient } from '@tanstack/react-query';
import useChat from '../../../../hooks/common/chat/useChat';
import { useFetchGroupChats, useFetchIndividualChats } from '../../../../hooks/common/chat/ChatQueryHooks';
import { useWebSocket } from '../../../../hooks/common/chat/useWebSocket';
import { useIndividualChatsStore } from '../../../../store/useIndividualChatStore';
import { useTranslation } from 'react-i18next';
import AcceptConfirmation from '@/components/Modal/Group/AcceptConfirmation.jsx';
import { useLocation } from 'react-router-dom';
import { useGroupChatsStore } from '@/store/useGroupChatStore';
import { useRef } from 'react';

const MemoizedChatSection = React.memo(ChatSection);

const TABS = [
  {
    title: 'chat-room.individuals',
    type: 'Individuals',
    icon: <ChatIcon />,
    className: 'rounded-l-full',
    content: <MemoizedChatSection />,
  },
  {
    title: 'chat-room.group',
    type: 'Group',
    icon: <GroupIcon />,
    className: '',
    content: <MemoizedChatSection />,
  },
  {
    title: 'chat-room.group-project',
    type: 'Group Project',
    icon: <GroupIcon />,
    className: 'rounded-r-full',
    content: <></>,
  },
];

export default function Chat({ hideProjectGroup }) {
  return (
    <ChatTypeProvider>
      <MessageProvider>
        <section className='h-[calc(100vh_-_104px)] min-h-0 overflow-hidden p-5 pb-0'>
          <PageHeader />
          <ChatTab hideProjectGroup={hideProjectGroup} />
        </section>
      </MessageProvider>
    </ChatTypeProvider>
  );
}

function PageHeader() {
  const { t } = useTranslation();
  return (
    <>
      <h3 className='mb-1 space-y-2 text-sm text-lightGray'>{t('chat')}</h3>
      <h1 className='text-xl font-bold text-titleColor'>{t('chat-room.canvas-chat')}</h1>
    </>
  );
}

function ChatTab({ hideProjectGroup }) {
  const [selectedIndex, setSelectedIndex] = useState(0);
  const [openConfirmation, setOpenConfirmation] = useState(false);
  const {
    setGroupChats,
    setGroupCurrentChat,
    groupCurrentChat,
    setChats,
    setIndividualCurrentChat,
    individualCurrentChat,
    currentChat,
  } = useChat();
  const { t } = useTranslation();
  const { setChatType } = useChatType();
  const userId = useUserStore((state) => state.getUserId());
  const queryClient = useQueryClient();
  const { addMessage } = useMessages();
  const location = useLocation();
  const otherUserId = useIndividualChatsStore((state) => state.currentOtherUserId);

  const { data: groupChats } = useFetchGroupChats((data) => data);
  const { data: individualChats } = useFetchIndividualChats((data) => data);

  const groupIds = useMemo(() => groupChats?.map((group) => group.groupId) || [], [groupChats]);
  const currentGroupId = useGroupChatsStore((state) => state.currentGroupId);
  const groupIdRef = useRef(currentGroupId);
  const otherUserIdRef = useRef(otherUserId);

  useEffect(() => {
    groupIdRef.current = currentGroupId;
  }, [currentGroupId]);

  useEffect(() => {
    otherUserIdRef.current = otherUserId;
  }, [otherUserId]);

  useEffect(() => {
    if (groupChats) {
      setGroupChats(groupChats);
      if (groupCurrentChat) {
        const updatedChat = groupChats.find((chat) => chat.groupId === groupCurrentChat.groupId);
        if (updatedChat) {
          setGroupCurrentChat(updatedChat);
        }
      }
    }
  }, [groupChats, groupCurrentChat?.groupId]);

  useEffect(() => {
    if (location.search.includes('inviteCode')) {
      setChatType('Group');
      setSelectedIndex(TABS.findIndex((t) => t.type === 'Group'));
      return setOpenConfirmation(true);
    }
  }, [location.search]);

  useEffect(() => {
    if (individualChats) {
      setChats(individualChats);
      if (individualCurrentChat) {
        setIndividualCurrentChat(
          individualChats.find(
            (chat) =>
              (chat.receiverId === userId ? chat.senderId : chat.receiverId) ===
              (individualCurrentChat.receiverId === userId
                ? individualCurrentChat.senderId
                : individualCurrentChat.receiverId)
          )
        );
      }
    }
  }, [individualChats, setIndividualCurrentChat]);

  const handleIndividualMessage = (message, senderId) => {
    console.log('handleIndividualMessage called:', {
      senderId,
      senderIdType: typeof senderId,
      otherUserIdRef: otherUserIdRef.current,
      otherUserIdRefType: typeof otherUserIdRef.current,
      isEqual: otherUserIdRef.current == senderId,
      isStrictEqual: otherUserIdRef.current === senderId,
      messageContent: message.content
    });
    // Always add message when we receive it via WebSocket (real-time update)
    addMessage(message);
    // Delay the query invalidation to avoid race condition
    setTimeout(() => {
      queryClient.invalidateQueries({ queryKey: ['individualChatsFetch'] });
    }, 1000);
  };

  const handleGroupMessage = (message, groupId) => {
    if (groupIdRef.current === groupId) {
      addMessage(message);
    }
    queryClient.invalidateQueries({ queryKey: ['groupChats'] });
    queryClient.invalidateQueries({ queryKey: ['group-messages', groupId] });
  };

  // const messageHandlers = useMemo(
  //   () => ({
  //     handleIndividualMessage: (message, senderId) => {
  //       if (otherUserId && otherUserId === senderId) {
  //         addMessage(message);
  //       }
  //       queryClient.invalidateQueries({ queryKey: ['individualChats'] });
  //       queryClient.invalidateQueries({ queryKey: ['messages', senderId] });
  //     },
  //     handleGroupMessage: (message, groupId) => {
  //       if (currentChat && currentChat.groupId === groupId) {
  //         addMessage(message);
  //       }
  //       console.log(currentChat, groupId)
  //       queryClient.invalidateQueries({ queryKey: ['groupChats'] });
  //       queryClient.invalidateQueries({ queryKey: ['group-messages', groupId] });
  //     },
  //   }),
  //   [addMessage, queryClient, currentChat]
  // );

  //connect websockets
  useWebSocket(userId, groupIds, handleIndividualMessage, handleGroupMessage);

  const handleOnChange = useCallback(
    (index) => {
      setSelectedIndex(index);
      setChatType(TABS[index].type);
    },
    [setChatType]
  );

  const visibleTabs = TABS.filter((tab) => !(hideProjectGroup && tab.type === 'Group Project'));

  return (
    <>
      <AcceptConfirmation
        open={openConfirmation}
        onClose={() => setOpenConfirmation(false)}
        url={location.pathname + location.search}
      />
      <TabGroup
        selectedIndex={selectedIndex}
        onChange={handleOnChange}
        className='flex h-full min-h-0 w-full flex-col pt-4'
      >
        <TabList className='mb-4 mt-2 flex flex-nowrap'>
          {visibleTabs.map((tab, index) => (
            <Tab
              key={tab.title}
              className={twMerge(
                `flex items-center gap-2 border px-3 py-2 text-sm/6 font-semibold text-slate-700 data-[selected]:bg-slate-100 data-[selected]:data-[hover]:bg-slate-100 data-[focus]:outline-1 focus:outline-none`,
                tab.className,
                hideProjectGroup && index === visibleTabs.length - 1 && 'rounded-r-full'
              )}
            >
              {tab.icon}
              <p>{t(tab.title)}</p>
            </Tab>
          ))}
        </TabList>
        <TabPanels className='flex min-h-0 w-full flex-1'>
          {TABS.map((tab) => (
            <TabPanel key={tab.title} className='flex h-full min-h-0 w-full flex-1' unmount={false}>
              {tab.content}
            </TabPanel>
          ))}
        </TabPanels>
      </TabGroup>
    </>
  );
}
