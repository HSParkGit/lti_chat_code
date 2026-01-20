import { Button, Dialog, DialogPanel, DialogTitle, Input } from '@headlessui/react';
import { useState } from 'react';
import { RxCrossCircled } from 'react-icons/rx';
import { useUserStore } from '../../../../../store/useUserStore';
import useChat from '../../../../../hooks/common/chat/useChat';
import { useCreateDirectChat, useSearchUsers } from '../../../../../hooks/common/chat/ChatQueryHooks';
import { useTranslation } from 'react-i18next';
import { useDebounce } from '../../../../../hooks/common/useDebounce';
import ButtonWithIcon from '@/components/common/ButtonWithIcon';
import { faXmark } from '@fortawesome/free-solid-svg-icons';

export default function CreateChatDialog({ children }) {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedMembers, setSelectedMembers] = useState([]);
  const { setCurrentChat, addNewChat, chats } = useChat();
  const userId = useUserStore((state) => state.getUserId());
  const user_id = userId && Number(userId);
  const courseId = useUserStore((state) => state.getCourseId());
  const { t } = useTranslation();
  const debouncedSearchQuery = useDebounce(searchQuery, 500);
  const { data: memberList, isFetching } = useSearchUsers(
    { search_term: debouncedSearchQuery, course_id: courseId },
    userId
  );
  function open() {
    setIsOpen(true);
  }

  function close() {
    setSearchQuery('');
    setIsOpen(false);
  }

  const removeSelectedMember = (memberId) => {
    setSelectedMembers(selectedMembers.filter((m) => m.id !== memberId));
  };
  const createDirectChat = useCreateDirectChat();
  const handleClick = (member) => () => {
    console.log('clicked member', member);
    console.log('existing chats', chats);
    const members = chats.map((chat) => chat.participants.find((participant) => participant.id !== user_id));
    const isMemberAlreadyExist = members.some((m) => m && m.id === member.id);
    if (isMemberAlreadyExist) {
      setCurrentChat(chats.find((chat) => chat.participants.some((participant) => participant.id === member.id)));
      close();
      return;
    }

    createDirectChat.mutate(member?.id, {
      onSuccess: (data) => {
        close();
        setCurrentChat(data);
        addNewChat(data);
      },
    });

    // const date = new Date();
    // const time = date.toISOString();
    // const newChat = {
    //   id: chats.length + 1,
    //   receiverName: member.name,
    //   receiverId: member.id,
    //   receiverProfileImage: member.profileImage,
    //   message: '',
    //   sentAt: time,
    //   read: true,
    //   messages: [],
    //   isLocalOnly: true,
    // };
    // // setCurrentChat(newChat);
    // addNewChat(newChat);
    // close();
  };

  return (
    <>
      <Button onClick={open} className='h-fit rounded-full bg-gray-200 p-2 hover:bg-gray-200'>
        {children}
      </Button>

      <Dialog open={isOpen} as='div' className='relative z-10 focus:outline-none' onClose={close}>
        <div className='fixed inset-0 z-10 w-screen overflow-y-auto bg-black/50'>
          <div className='flex min-h-full items-center justify-center p-4'>
            <DialogPanel
              transition
              className='border-1 data-[closed]:transform-[scale(95%)] w-full max-w-xl rounded-xl border bg-white duration-300 ease-out data-[closed]:opacity-0'
            >
              <DialogTitle className='relative rounded-t-xl border-b bg-slate-50 p-6 text-base/7'>
                {/* <RxCrossCircled className='absolute cursor-pointer right-2 top-2' onClick={close} size={24} /> */}
                <p className='text-lg font-bold text-slate-800'>{t('chat-room.create-new-chat')}</p>
                <p className='text-base text-[#667085]'>{t('chat-room.start-message')}</p>
              </DialogTitle>
              <div className='flex flex-col gap-4 p-6'>
                <label className='block text-lg font-semibold text-slate-800'>{t('chat-room.to')}</label>
                <div className='flex min-h-[40px] flex-wrap gap-2 rounded-full border p-2'>
                  {selectedMembers.map((member) => (
                    <div
                      key={member.id}
                      className='flex items-center gap-1 rounded-full bg-green-500 px-2 py-1 text-white'
                    >
                      <span>{member.name}</span>
                      <button onClick={() => removeSelectedMember(member.id)} className='text-white'>
                        ✕
                      </button>
                    </div>
                  ))}
                  <Input
                    className='flex-1 border-none bg-transparent indent-2 outline-none placeholder:text-sm'
                    placeholder={t('chat-room.name-login-id-sis-id')}
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                  />
                </div>

                {memberList && memberList.length > 0 ? (
                  <div className='flex max-h-[250px] flex-col overflow-y-auto'>
                    {memberList.map((member) => (
                      <div
                        key={member.id}
                        onClick={handleClick(member)}
                        className='flex cursor-pointer items-center gap-4 border-b py-2 hover:bg-gray-100'
                      >
                        {member.profileImage ? (
                          <div className='h-12 w-12 overflow-hidden rounded-full'>
                            <img src={member.profileImage} className='h-full w-full object-cover' />
                          </div>
                        ) : (
                          <p className='flex h-12 w-12 items-center justify-center rounded-full bg-gray-200 text-gray-500'>
                            {member.name?.[0]}
                          </p>
                        )}
                        <div>
                          <p className='text-sm font-semibold'>{member.name}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className='text-center text-sm text-gray-500'>
                    {isFetching ? 'Searching...' : t('chat-room.no-member-found')}
                  </p>
                )}

                <div className='mt-4 flex justify-end'>
                  <ButtonWithIcon
                    label={t('common.cancel')}
                    variant='btn-bordered'
                    onClick={close}
                    icon={faXmark}
                    iconPosition='right'
                  />
                </div>
              </div>
            </DialogPanel>
          </div>
        </div>
      </Dialog>
    </>
  );
}
