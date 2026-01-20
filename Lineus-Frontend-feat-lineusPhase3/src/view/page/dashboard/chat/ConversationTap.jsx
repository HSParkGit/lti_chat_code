import { twMerge } from 'tailwind-merge';
import { useChatType } from './providers/useChatTypeProvider';
import { formatChatTimestamp } from '../../../../libs/utils/chat/date.js';
import { useUserStore } from '../../../../store/useUserStore.js';
import { ChecksBlueIcon } from '../../../../components/common/Icon.jsx';
import { FiBellOff } from 'react-icons/fi';
import { useTranslation } from 'react-i18next';
import { useMemo } from 'react';
import { useMarkAsRead } from '@/hooks/common/chat/ChatQueryHooks';
import { useQueryClient } from '@tanstack/react-query';
import { useIndividualChatsStore } from '@/store/useIndividualChatStore';

export default function ConversationTap({ chat, setCurrentChat, currentChat, unread }) {
  const { t } = useTranslation();
  const { isIndividual, isGroup } = useChatType();
  const userId = useUserStore((state) => state.getUserId());
  const user_id = userId && Number(userId);
  const queryClient = useQueryClient();
  const user = useMemo(() => {
    return isIndividual
      ? chat.participants.filter((participant) => participant.id !== user_id)[0]
      : { id: chat.groupId, name: chat.groupName, senderId: chat.senderId, senderName: chat.senderName };
  }, [isIndividual, chat, userId]);
  const markAsRead = useMarkAsRead();
  // const handleClick = () => {
  //   setCurrentChat(chat);
  //   markAsRead.mutate(chat.chat_id, {
  //     onSuccess: () => {
  //       queryClient.invalidateQueries({ queryKey: ['conversations'] });
  //     },
  //     onError: (error) => {
  //       console.error('Error marking chat as read:', error);
  //     },
  //   });
  // };

  const resetUnread = useIndividualChatsStore((s) => s.resetUnread);

  const handleClick = () => {
    setCurrentChat(chat);

    // 🔹 Optimistically clear unread UI
    resetUnread(chat.chat_id);

    markAsRead.mutate(chat.chat_id, {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['conversations'] });
        queryClient.invalidateQueries({ queryKey: ['messages', chat.chat_id] });
      },
      onError: (err) => {
        console.error(err);
      },
    });
  };

  return (
    <div
      onClick={handleClick}
      className={twMerge(
        'flex cursor-pointer items-center justify-between gap-4 border border-[#ECF1F6] p-4',
        currentChat && currentChat.chat_id === chat.chat_id ? 'bg-[#FAFAFA]' : 'hover:bg-gray-100'
      )}
    >
      <div className='flex w-full gap-4'>
        {user?.avatar_url ? (
          <div className='h-12 w-12 overflow-hidden rounded-full'>
            <img src={user?.avatar_url} className='h-full w-full object-cover' />
          </div>
        ) : (
          <p className='flex h-12 w-12 items-center justify-center rounded-full bg-gray-200 text-gray-500'>
            {user?.name?.[0]}
          </p>
        )}
        <div className='relative flex flex-1 flex-col gap-1'>
          <div className='flex w-full items-center justify-between'>
            <h5 className='font-bold'>{user?.name}</h5>
            <div className='flex items-center gap-2'>
              <p className='text-xs text-gray-400'>
                {chat?.sentAt && t ? formatChatTimestamp(chat.created_at, t) : ''}
              </p>

              {unread !== 0 && (
                <span className='flex items-center justify-center rounded-lg bg-red-600 px-2 text-xs text-white'>
                  {(unread || 0) > 9 ? (
                    <>
                      <span>9</span>
                      <span>+</span>
                    </>
                  ) : (
                    unread
                  )}
                </span>
              )}

              {chat.muted && <FiBellOff className='text-gray-400' />}
            </div>
          </div>
          <p
            style={{
              display: '-webkit-box',
              WebkitLineClamp: 2,
              WebkitBoxOrient: 'vertical',
              overflow: 'hidden',
              textOverflow: 'ellipsis',
            }}
            className={twMerge(
              'overflow-wrap-anywhere break-before-all pr-4 text-sm text-[#757B84]',
              unread === 0 ? 'font-semibold text-gray-800' : ''
            )}
          >
            {isGroup && chat?.last_message
              ? `${chat.senderId === user_id ? 'You' : chat.senderName}: ${chat.last_message}`
              : chat?.last_message?.content}
          </p>
          {unread === 0 && chat?.last_message && <ChecksBlueIcon className='absolute bottom-[2px] right-1 h-4 w-4' />}
        </div>
      </div>
    </div>
  );
}
