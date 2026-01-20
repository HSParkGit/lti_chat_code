import { Button, Popover, PopoverButton, PopoverPanel } from '@headlessui/react';
import { ConversationIcon, MuteIcon } from '../../../../components/common/Icon';
import { FiLogOut } from 'react-icons/fi';
import { useLeaveGroup } from '../../../../hooks/student/useGroup';
import toast from 'react-hot-toast';
import { useQueryClient } from '@tanstack/react-query';
import useChat from '../../../../hooks/common/chat/useChat';
import { useMarkAsRead, useMuteGroup, useUnmuteGroupChat } from '../../../../hooks/common/chat/ChatQueryHooks';
import { useTranslation } from 'react-i18next';

export default function GroupChatSetting({ chatData }) {
  const { setCurrentChat } = useChat();
  const { mutate: leave } = useLeaveGroup();
  const markAsRead = useMarkAsRead();
  const muteGroupChat = useMuteGroup();
  const unmuteGroupChat = useUnmuteGroupChat();
  const queryClient = useQueryClient();
  const { t } = useTranslation();

  // const handleMarkAsRead = (close) => {
  //   messages.forEach((msg) => {
  //     if (!msg.read) {
  //       markAsRead.mutate(msg.id, {
  //         onSuccess: () => {
  //           queryClient.invalidateQueries({ queryKey: ['groupChats'] });
  //         },
  //       });
  //     }
  //   });
  //   close();
  // };

  const handleMuteGroupChat = (close) => {
    const action = chatData.muted ? unmuteGroupChat : muteGroupChat;
    action.mutate(chatData.groupId, {
      onSuccess: () => {
        toast.success(t(`chat-room.action-response.${chatData.muted ? 'unmute' : 'mute'}-success`));
        queryClient.invalidateQueries({ queryKey: ['groupChats'] });
        close();
      },
      onError: (error) => {
        toast.success(t(`chat-room.action-response.${chatData.muted ? 'unmute' : 'mute'}-fail`));
        console.error('Error muting group chat:', error);
      },
    });
  };

  const leaveGroup = (close) => {
    leave(chatData.groupId, {
      onSuccess: () => {
        toast.success(t('chat-room.action-response.leave-group-success'));
        queryClient.invalidateQueries({ queryKey: ['groupChats'] });
        setCurrentChat(null);
        close();
      },
      onError: (error) => {
        toast.error(t('chat-room.action-response.leave-group-fail'));
        console.error('Error leaving group:', error);
      },
    });
  };

  const menuItems = [
    // {
    //   id: 'mark-as-read',
    //   icon: <ConversationIcon />,
    //   label: 'chat-room.mark-as-read',
    //   action: handleMarkAsRead,
    // },
    {
      id: 'mute',
      icon: <MuteIcon />,
      label: chatData.muted ? 'chat-room.unmute-notification' : 'chat-room.mute-notification',
      action: handleMuteGroupChat,
    },
    // {
    //   id: 'divider',
    //   type: 'divider',
    // },
    {
      id: 'leave',
      icon: <FiLogOut />,
      label: 'chat-room.leave-group',
      action: leaveGroup,
    },
  ];

  return (
    <Popover className='relative'>
      {({ close }) => (
        <>
          <PopoverButton className='h-10 w-10 rounded-full p-2 hover:bg-gray-200'>
            <span className='block'>⋮</span>
          </PopoverButton>
          <PopoverPanel className='absolute right-1/2 mt-2 flex w-64 flex-col rounded-md border bg-white p-2 shadow-md'>
            {menuItems.map((item) =>
              item.type === 'divider' ? (
                <div key={item.id} className='h-[1px] w-full bg-gray-200'></div>
              ) : (
                <Button
                  key={item.id}
                  className='flex items-center gap-2 rounded p-2 text-start text-sm hover:bg-gray-100'
                  onClick={() => item.action(close)}
                >
                  {item.icon}
                  <span>{t(item.label)}</span>
                </Button>
              )
            )}
          </PopoverPanel>
        </>
      )}
    </Popover>
  );
}
