import { Button, Popover, PopoverButton, PopoverPanel } from '@headlessui/react';
import { ConversationIcon, MuteIcon } from '../../../../components/common/Icon';
import { FiLogOut, FiTrash } from 'react-icons/fi';
import toast from 'react-hot-toast';
import { useQueryClient } from '@tanstack/react-query';
import { useUserStore } from '../../../../store/useUserStore';
import { useIndividualChatsStore } from '../../../../store/useIndividualChatStore';
import { useDeleteChat, useMarkAsRead, useMuteChat, useUnmuteChat } from '../../../../hooks/common/chat/ChatQueryHooks';
import useChat from '../../../../hooks/common/chat/useChat';
import { useTranslation } from 'react-i18next';

export default function IndividualChatSetting({ chat, user }) {
  const { setCurrentChat } = useChat();
  const deleteChatMutation = useDeleteChat();
  const queryClient = useQueryClient();
  const markAsReadMutation = useMarkAsRead();
  const muteChatMutation = useMuteChat();
  const unmuteChatMutation = useUnmuteChat();
  const userId = useUserStore((state) => state.getUserId());
  const removeNewChat = useIndividualChatsStore((state) => state.removeNewChat);
  const { t } = useTranslation();

  // const handleMarkAsRead = (close) => {
  //   messages.forEach((msg) => {
  //     const self = msg.senderId === userId;
  //     if (!msg.read && !self) {
  //       markAsReadMutation.mutate(msg.id, {
  //         onSuccess: () => {
  //           queryClient.invalidateQueries({ queryKey: ['individualChats'] });
  //         },
  //       });
  //     }
  //   });
  //   close();
  // };

  const handleDeleteChat = (close) => {
    deleteChatMutation.mutate(chat.chat_id, {
      onSuccess: () => {
        toast.success(t(`chat-room.action-response.delete-chat-success`));
        setCurrentChat(null);
        removeNewChat(user.id);
        queryClient.invalidateQueries({ queryKey: ['individualChats'] });
        useIndividualChatsStore.getState().deleteChatFromStore(chat.chat_id);
        close();
      },
      onError: (error) => {
        toast.success(t(`chat-room.action-response.delete-chat-fail`));
        console.error('Error deleting chat:', error);
      },
    });
  };

  const handleMuteChat = (close) => {
    if (!chat?.chat_id) return;

    const action = chat.muted ? unmuteChatMutation : muteChatMutation;
    const mode = chat.muted ? 'unmute' : 'mute';

    action.mutate(chat.chat_id, {
      onSuccess: () => {
        toast.success(t(`chat-room.action-response.${mode}-success`));

        // still refetch in background (optional but safe)
        queryClient.invalidateQueries({ queryKey: ['individualChats'] });

        if (chat.muted) {
          useIndividualChatsStore.getState().resetUnMute(chat.chat_id);
        } else {
          useIndividualChatsStore.getState().resetMute(chat.chat_id);
        }

        close();
      },
      onError: (error) => {
        toast.error(t(`chat-room.action-response.${mode}-fail`));
        console.error(`Error trying to ${mode} chat:`, error);
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
      label: chat.muted ? 'chat-room.unmute-notification' : 'chat-room.mute-notification',
      action: handleMuteChat,
    },
    // {
    //   id: 'divider',
    //   type: 'divider',
    // },
    {
      id: 'delete',
      icon: <FiTrash />,
      label: 'chat-room.delete-conversation',
      action: handleDeleteChat,
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
