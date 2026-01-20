import { Button, Input } from '@headlessui/react';
import ChatTopUserBar from './ChatTopUserBar';
import MessageList from './MessageList';
import { SendIcon } from '../../../../components/common/Icon';
import { useQueryClient } from '@tanstack/react-query';
import { useChatType } from './providers/useChatTypeProvider';
import { useSendGroupMessage, useSendMessage } from '../../../../hooks/common/chat/ChatQueryHooks';
import { useTranslation } from 'react-i18next';
import Picker from '@emoji-mart/react';
import data from '@emoji-mart/data';
import { useEffect, useRef, useState } from 'react';
import { FiSmile } from 'react-icons/fi';
import TextArea from '@/components/common/TextArea.jsx';
import { memo } from 'react';

const MemoizedMessageList = memo(MessageList);

export default function MessageContainer({ currentChat, chats, user, onSendSuccess, onInputChange, messageInput }) {
  const sendMessageMutation = useSendMessage();
  const sendGroupMessageMutation = useSendGroupMessage();
  const queryClient = useQueryClient();
  const { isGroup } = useChatType();
  const { t } = useTranslation();
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const pickerRef = useRef(null);
  const buttonRef = useRef(null);
  const [messageSent, setMessageSent] = useState(false);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (pickerRef.current && !pickerRef.current.contains(event.target) && !buttonRef.current.contains(event.target)) {
        setShowEmojiPicker(false);
      }
    };

    document.addEventListener('mouseup', handleClickOutside);
    return () => document.removeEventListener('mouseup', handleClickOutside);
  }, []);

  const isLoading = sendMessageMutation.isPending || sendGroupMessageMutation.isPending;

  const addEmoji = (emoji) => {
    onInputChange(user?.id, messageInput + emoji.native);
  };
  // helper for resizing input
  const handleInputResize = () => {
    setMessageSent(true); //help resize input
    setTimeout(() => setMessageSent(false), 100); // Reset after a short delay
  };
  const handleSendMessage = () => {
    if (!user.id || !messageInput.trim() || !currentChat || isLoading) return;
    if (isGroup) {
      const groupMessage = {
        groupId: currentChat.groupId,
        message: messageInput,
      };

      sendGroupMessageMutation.mutate(groupMessage, {
        onSuccess: () => {
          queryClient.invalidateQueries({ queryKey: ['groupChats'] });
          queryClient.invalidateQueries({ queryKey: ['groupChat'] });
          onSendSuccess(user.id); // Parent clears the input
          handleInputResize();
        },
        onError: (error) => {
          console.error('Message send error:', error);
        },
      });

      return;
    }
    const message = {
      chat_id: currentChat.chat_id,
      content: messageInput,
      reply_to_message_id: null,
      message_type: 'text',
    };

    sendMessageMutation.mutate(message, {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['individualChats'] });
        onSendSuccess(user.id); // Parent clears the input
        handleInputResize();
      },
      onError: (error) => {
        console.error('Message send error:', error);
      },
    });
  };

  const handleKeyDown = (e) => {
    // Skip if IME is composing (한글 입력 중)
    if (e.nativeEvent?.isComposing || e.isComposing) {
      return;
    }
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      if (messageInput.trim()) {
        handleSendMessage();
      }
    }
  };

  return (
    <div className='flex h-full min-h-0 w-full flex-1 flex-col'>
      <ChatTopUserBar currentChat={currentChat} chats={chats} user={user} groupId={currentChat?.groupId ?? null} />
      <MemoizedMessageList />
      <div className='flex min-h-0 w-full flex-shrink-0 items-center justify-between gap-2 p-4'>
        <div className='relative w-full'>
          <Input
            as={TextArea}
            sent={messageSent}
            messageInput={messageInput}
            user={user}
            onInputChange={onInputChange}
            // className='overflow-wrap-anywhere h-[42px] max-h-[650px] w-full resize-none break-before-all overflow-auto rounded-xl border py-2 pl-4 pr-8 indent-4 shadow-md placeholder:text-sm focus:border-green-400 focus:outline-none'
            placeholder={t('chat-room.enter-your-response')}
            handleKeyDown={handleKeyDown}
          />

          {/* Emoji button inside the input box */}
          <button
            ref={buttonRef}
            type='button'
            onClick={() => setShowEmojiPicker(!showEmojiPicker)}
            className='absolute right-3 top-1/2 -translate-y-1/2 text-xl'
          >
            <FiSmile />
          </button>

          {/* Emoji picker */}
          {showEmojiPicker && (
            <div ref={pickerRef} className='absolute bottom-full right-0 z-50 mb-2'>
              <Picker data={data} onEmojiSelect={addEmoji} />
            </div>
          )}
        </div>

        <Button
          className='h-fit cursor-pointer rounded-full bg-lightGreen p-2'
          onClick={handleSendMessage}
          disabled={!messageInput.trim() || isLoading}
        >
          <SendIcon fill={'white'} />
        </Button>
      </div>
    </div>
  );
}
