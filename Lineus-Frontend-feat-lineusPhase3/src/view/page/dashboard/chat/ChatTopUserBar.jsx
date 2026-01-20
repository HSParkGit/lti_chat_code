import React from 'react';
import IndividualChatSetting from './IndividualChatSetting';
import { useChatType } from './providers/useChatTypeProvider';
import GroupChatSetting from './GroupChatSetting';
import { useTranslation } from 'react-i18next';
import { useGroupMembers } from '@/hooks/group/agenda/useGroupMembers';

export default function ChatTopUserBar({ currentChat, user, groupId }) {
  const { data: members, isLoading: memberIsLoading } = useGroupMembers(groupId);

  const ChatTypeContext = useChatType();
  const { t } = useTranslation();
  return (
    <div className='border-b-1 flex min-h-0 w-full justify-between border border-x-0 border-t-0 border-slate-200 px-6 py-4'>
      <div className='flex items-center gap-4'>
        {user?.avatar_url ? (
          <div className='h-12 w-12 overflow-hidden rounded-full'>
            <img src={user.avatar_url} className='h-full w-full object-cover' />
          </div>
        ) : (
          <p className='flex h-12 w-12 items-center justify-center rounded-full bg-gray-200 text-gray-500'>
            {user?.name?.[0]}
          </p>
        )}
        <div className='h-full'>
          <p className='font-bold'>{user?.name}</p>
          {groupId && members?.length > 0 && (
            <p className='text-xs text-gray-600'>
              {' '}
              {members?.length} {t('group.members')}
            </p>
          )}
          {/* <p className='text-xs text-gray-600'>{t('chat-room.last-online')} 4 hours ago</p> */}
        </div>
      </div>
      {ChatTypeContext.chatType === 'Group' ? (
        <GroupChatSetting chatData={currentChat} />
      ) : (
        <IndividualChatSetting chat={currentChat} user={user} />
      )}
    </div>
  );
}
