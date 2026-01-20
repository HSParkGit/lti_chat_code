import { twMerge } from 'tailwind-merge';
import { useChatType } from './providers/useChatTypeProvider';
import { useUserStore } from '../../../../store/useUserStore';
import { formatChatTimestampExact12Hour } from '../../../../libs/utils/chat/date.js';
import { useMemo } from 'react';
import { ChecksBlackIcon, ChecksWhiteIcon } from '../../../../components/common/Icon.jsx';
import { renderMessageContent } from '@/utils/renderMessageContent.jsx';
import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';

/**
 * A functional component that renders a chat message with styling based on whether the message is sent by the user or received.
 *
 * @param {{message: {
 *  id: number;
 *  message: string;
 * read: boolean;
 *  receiverId: number;
 *  receiverName: string;
 *  receiverProfileImage: string;
 *  sender_id: number;
 *  sender_name: string;
 *  senderProfileImage: string;
 *  sentAt: string;
 * }}} props - The message object containing details like id, text, time, and sender information.
 * @return {JSX.Element} A styled message component.
 */

export default function Message({ message }) {
  const { getUserId } = useUserStore();
  const user_id = getUserId() && Number(getUserId());
  const queryClient = useQueryClient();
  const ChatTypeContext = useChatType();
  const self = useMemo(() => message.sender_id === user_id, [message.sender_id, user_id]);
  const navigate = useNavigate();
  console.log(message, 'each message id');
  console.log(self, 'is self message');
  return (
    <>
      {/*<AcceptConfirmation open={openAccept} onClose={() => setOpenAccept(false)} url={url} />*/}
      {ChatTypeContext.chatType === 'Group' && !self ? (
        <div className='w-max-[90%] flex w-fit gap-4 rounded-lg p-2 text-slate-600'>
          {message.senderProfileImage ? (
            <div className='h-8 w-8 flex-shrink-0 overflow-hidden rounded-full'>
              <img src={message.senderProfileImage} className='h-full w-full object-cover' />
            </div>
          ) : (
            <p className='flex max-h-8 min-h-8 min-w-8 items-center justify-center rounded-full border text-slate-600'>
              {message.sender_name[0]}
            </p>
          )}
          <div className='flex flex-col gap-1'>
            <div className='flex items-center gap-1'>
              <p className='text-sm font-bold'>{message.sender_name}</p>
              <p className='text-xs text-gray-600'>{formatChatTimestampExact12Hour(message.sentAt)}</p>
            </div>
            <p className='overflow-wrap-anywhere max-w-[90%] break-before-all gap-1 rounded-lg border border-slate-200 bg-slate-50 p-2 font-medium text-slate-600'>
              {renderMessageContent(message.content, navigate, queryClient)}
            </p>
          </div>
        </div>
      ) : (
        <div
          key={message.id}
          className={twMerge(
            'flex gap-4',
            message.sender_id === user_id ? 'items-end justify-end' : 'items-start justify-start'
          )}
        >
          <div
            className={twMerge(
              'flex max-w-[90%] flex-col gap-1 rounded-lg p-2 font-medium',
              message.sender_id === user_id ? 'bg-lightGreen text-white' : 'border border-slate-200 text-slate-600'
            )}
          >
            <p className={'overflow-wrap-anywhere break-before-all'}>
              {renderMessageContent(message.content, navigate, queryClient)}
            </p>
            <div className='flex items-end gap-1'>
              <p className='ms-auto text-xs'>{formatChatTimestampExact12Hour(message.timestamp)}</p>
              {self ? <ChecksWhiteIcon /> : <ChecksBlackIcon />}
            </div>
          </div>
        </div>
      )}
    </>
  );
}
